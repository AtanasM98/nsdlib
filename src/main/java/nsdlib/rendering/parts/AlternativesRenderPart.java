package nsdlib.rendering.parts;

import java.util.*;

import nsdlib.elements.NSDElement;
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
    private int caseWidth;
    private Size size;
    private int headingHeight;
    private List<RenderColor> caseColors;

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
    public int getCaseWidth() { return caseWidth; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AlternativesRenderPart that = (AlternativesRenderPart) o;
        return caseWidth == that.caseWidth && headingHeight == that.headingHeight && Objects.equals(label, that.label) && Objects.equals(pathLabels, that.pathLabels) && Objects.equals(content, that.content) && Objects.equals(size, that.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), label, pathLabels, content, caseWidth, size, headingHeight);
    }

    @Override
    public void setBackground(RenderColor color) {
        this.background = color;
        this.content.setBackground(RenderColor.WHITE);
    }

    public void setBackgroundCase(RenderColor color, int index) {
        try {
            this.caseColors.set(index, color);
        }catch (IndexOutOfBoundsException e) {
            if(index == this.caseColors.size()) {
                this.caseColors.add(color);
            }
        }
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

        adapter.fillRect(x, y, w, headingHeight, getBackground());
        y += drawHeading(adapter, x, y, w);
        content.render(adapter, x, y, w);
    }

    private int drawHeading(RenderAdapter<?> a, int x, int y, int w)
    {
        int triangleHeight;
        if(this.content.getChildren().size() > 2)
            triangleHeight = headingHeight / 2;
        else
            triangleHeight = headingHeight;

        caseWidth = w / pathLabels.size();
        int lastSepX = x + w - caseWidth;
        if(this.content.getChildren().size() > 2)
            y += triangleHeight;
        else
            y += triangleHeight / 2;

        // a^2 + b^2 = c^2
        int dx = lastSepX - x, dy = headingHeight;
        double hypotLength = Math.sqrt(dx * dx + dy * dy);
        // tan of angle between x-axis and hypotenuse
        double linkAngleTan = Math.tan(Math.asin(triangleHeight / hypotLength));
        for(int i = 0, n = pathLabels.size(); i < n; ++i) {
            if (this.content.getChildren().size() > 2)
                a.fillRect(x + (caseWidth * i), y - triangleHeight, caseWidth, headingHeight, this.caseColors.get(i));
            else
                a.fillRect(x + (caseWidth * i), y - triangleHeight / 2, caseWidth, headingHeight, this.caseColors.get(i));
        }

        for (int i = 0, n = pathLabels.size(); i < n; ++i) {
            a.drawStringCentered(pathLabels.get(i), x + caseWidth / 2, y);
            x += caseWidth;

            // for all but last case (since it doesn't need vertical separators)
            if (i < n - 1 && pathLabels.size() > 2) {
                // calc. amount of pixels that current point is above link end
                int adjacent = (int) Math.abs(linkAngleTan * (x - lastSepX));
                a.drawLine(x, y - adjacent, x, y + triangleHeight);
            }
        }

        x -= caseWidth * pathLabels.size();
        if(this.content.getChildren().size() > 2)
            y -= triangleHeight;
        else
            y -= triangleHeight / 2;

        a.drawRect(x, y, w, headingHeight);
        int[] xSet = new int[]{lastSepX, x + w, x};
        int[] ySet = new int[]{y + triangleHeight, y, y};
        a.fillPolygon(xSet, ySet, 3, this.background);
        a.drawPolygon(xSet, ySet, 3);
        a.drawStringCentered(label, lastSepX, y);

        return headingHeight;
    }
}
