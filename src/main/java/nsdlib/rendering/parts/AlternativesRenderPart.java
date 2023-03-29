package nsdlib.rendering.parts;

import java.sql.Array;
import java.util.*;

import nsdlib.elements.NSDElement;
import nsdlib.elements.alternatives.NSDCase;
import nsdlib.rendering.RenderColor;
import nsdlib.rendering.Size;
import nsdlib.rendering.parts.ContainerRenderPart.Orientation;
import nsdlib.rendering.renderer.RenderAdapter;
import nsdlib.rendering.renderer.RenderContext;


/**
 * Render part for Case and Decision elements, drawing the header and the child
 * containers.
 */
public class AlternativesRenderPart extends RenderPart implements IContainerHolderRenderPart
{
    private final String label;
    private final List<String> pathLabels;
    private final ContainerRenderPart content;
    private int[] caseWidths;
    private Size size;
    private int headingHeight;
    private final List<RenderColor> caseColors;

    /**
     * Constructs a new alternatives part.
     *
     * @param source This part's source element.
     * @param label The alternative's condition label.
     * @param pathLabels The labels of all possible cases.
     * @param pathContents The child parts.
     */
    public AlternativesRenderPart(NSDElement source, String label,
            Collection<String> pathLabels, Collection<RenderPart> pathContents)
    {
        super(source);

        this.label = label;
        this.pathLabels = Collections.unmodifiableList(new ArrayList<>(pathLabels));
        this.caseColors = new ArrayList<RenderColor>(pathLabels.size());

        this.content = new ContainerRenderPart(Orientation.HORIZONTAL, pathContents);
    }

    public int getHeadingHeight() {
        if(this.content.getChildren().size() > 2){
            return headingHeight / 2;
        } else {
            return headingHeight;
        }
    }

    public int[] getCaseWidths() { return caseWidths; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AlternativesRenderPart that = (AlternativesRenderPart) o;
        return Arrays.equals(this.caseWidths, that.caseWidths) && headingHeight == that.headingHeight && Objects.equals(label,
                that.label) && Objects.equals(pathLabels, that.pathLabels) && Objects.equals(content, that.content) && Objects.equals(size, that.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), label, pathLabels, content, Arrays.hashCode(caseWidths), size, headingHeight);
    }

    @Override
    public void setBackground(RenderColor color) {
        this.background = color;
        this.content.setBackground(RenderColor.WHITE);
        setBackgroundCases(color);
    }

    public void setBackgroundCase(RenderColor color, int index) {
        if(index == this.caseColors.size()) {
            this.caseColors.add(color);
        }
        this.caseColors.set(index, color);
    }

    public void setBackgroundCases(RenderColor color) {
        this.caseColors.replaceAll(ignored -> color);
    }

    public RenderColor getBackgroundCase(int index) {
        return this.caseColors.get(index);
    }

    @Override
    public ContainerRenderPart getContent() { return content; }

    public void setBackgroundChild(RenderColor color, int index) {
        this.content.setBackgroundChild(color, index);
    }

    @Override
    public RenderPart findForSource(NSDElement source)
    {
        return source == getSource() ? this : content.findForSource(source);
    }

    @Override
    public void layout(RenderContext ctx)
    {
        content.layout(ctx);

        Size box = ctx.box(label);
        Size contentSize = content.getSize();

        headingHeight = box.height * 2;

        width = Math.max(box.width * pathLabels.size(), contentSize.width);
        int height = headingHeight + contentSize.height;

        size = new Size(width, height);
    }

    @Override
    public Size getSize()
    {
        return size;
    }

    @Override
    public void setSize(Size s) {
        size = s;
    }

    @Override
    public void render(RenderAdapter<?> adapter, int x, int y, int w)
    {
        positionX = x;
        positionY = y;
        width = w;
        this.size = new Size(w, size.height);

        adapter.fillRect(x, y, w, headingHeight, RenderColor.TRANSPARENT);
        y += drawHeading(adapter, x, y, w);
        content.render(adapter, x, y, w);
    }

    private int drawHeading(RenderAdapter<?> a, int x, int y, int w)
    {
        boolean caseRender = true;
        if(this.getSource() != null) {
            caseRender = this.getSource().getClass().equals(NSDCase.class);
        }
        caseWidths = content.createChildWidths(w);
        if(caseWidths.length > 0) {
            int originalX = x;
            int originalY = y;
            int triangleHeight = ((caseRender) ? headingHeight / 3: headingHeight);
            int lastSepX = x + w - caseWidths[caseWidths.length - 1];
            y += ((caseRender) ? triangleHeight: triangleHeight / 2);

            paintCaseColors(a, x, caseWidths, originalY);

            // a^2 + b^2 = c^2
            int dx = lastSepX - x, dy = headingHeight;
            double hypotLength = Math.sqrt(dx * dx + dy * dy);
            // tan of angle between x-axis and hypotenuse
            double linkAngleTan = Math.tan(Math.asin(triangleHeight / hypotLength));

            for (int i = 0, n = pathLabels.size(); i < n; ++i) {
                if(caseRender) {
                    a.drawStringCentered(pathLabels.get(i), x + caseWidths[i] / 2, y + (triangleHeight / 2));
                } else {
                    a.drawStringCentered(pathLabels.get(i), x + caseWidths[i] / 2, y);
                }
                x += caseWidths[i];

                // for all but last case (since it doesn't need vertical separators)
                if (i < n - 1 && caseRender) {
                    // calc. amount of pixels that current point is above link end
                    int adjacent = (int) Math.abs(linkAngleTan * (x - lastSepX));
                    a.drawLine(x, y + triangleHeight - (adjacent * 2), x, y + (triangleHeight * 2));
                }
            }

            x = originalX;
            y = originalY;
            a.drawRect(x, y, w, headingHeight);
            int[] xSet = new int[]{lastSepX, x + w, x};
            int[] ySet = new int[]{y + triangleHeight, y, y};
            if(caseRender) {
                ySet = new int[]{y + (triangleHeight * 2), y, y};
            }
            a.fillPolygon(xSet, ySet, 3, this.background);
            a.drawPolygon(xSet, ySet, 3);

            a.drawStringCentered(label, x + width / 2 , y - 5);
        }
        return headingHeight;
    }

    private void paintCaseColors(RenderAdapter<?> a, int x, int[] caseWidths, int originalY) {
        if(!this.caseColors.isEmpty()) {
            for(int i = 0; i < caseColors.size(); ++i) {
                a.fillRect(x, originalY, caseWidths[i], headingHeight,
                        this.caseColors.get(i));
                x += caseWidths[i];
            }
        }
    }
}
