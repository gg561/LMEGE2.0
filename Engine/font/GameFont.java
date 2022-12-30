package font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import textures.Texture;

public class GameFont {
	
	private static final String RESOURCE_LOC = "/res/textures/fonts/";
	
	public static final int FONT_ATLAS_WIDTH = 260;
	public static final int FONT_ATLAS_HEIGHT = 150;

	private String format;
	private String name;
	private int fontType;
	private int size;
	private Font font;
    private int width = 0;
    private int height;
    private int lineHeight;
    private Texture fontTexture;
    private File textureOutput;
	
	private Map<Integer, Glyph> characterInfo = new HashMap<Integer, Glyph>();
	
	public GameFont(String name, String format, int fontType, int size) {
		this.name = name;
		this.fontType = fontType;
		this.size = size;
		this.format = format;
		this.font = new Font(name, fontType, size);
	}
	
	public GameFont(Font font) {
		this.font = font;
		this.name = font.getFontName();
		this.fontType = font.getStyle();
		this.size = font.getSize();
		this.format = "png";
	}
	
	public void createAsResource() {
		try {
			textureOutput = new File(System.getProperty("user.dir") + RESOURCE_LOC + name + "." + format);
			textureOutput.createNewFile();
			fontTexture = Texture.loadFromImage(createFontAtlas(textureOutput));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Map<Integer, Glyph> getCharacterInfo() {
		return characterInfo;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getLineHeight() {
		return lineHeight;
	}

	public BufferedImage createFontAtlas(File output) {
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		g2d.setFont(font);
		FontMetrics fm = g2d.getFontMetrics();
	    fm = g2d.getFontMetrics();
	    int estimatedWidth = (int) (Math.sqrt(font.getNumGlyphs()) * font.getSize() + 1);
	    height = fm.getHeight();
	    lineHeight = fm.getHeight();
	    int x = 0;
	    int y = (int) (fm.getHeight() * 1.4f);
	    for(int i = 0; i < font.getNumGlyphs(); i++) {
	    	if(font.canDisplay(i)) {
	    		int charWidth = fm.charWidth(i);
	    		Glyph g = new Glyph(x, y, charWidth, lineHeight);
	    		characterInfo.put(i, g);
	    		width = Math.max(x + charWidth, width);
	    		
	    		x += g.getWidth();
	    		if(x > estimatedWidth) {
	    			x = 0;
	    			y += fm.getHeight() * 1.4f;
	    			height += fm.getHeight() * 1.4f;
	    		}
	    	}
	    }
		height += fm.getHeight() * 1.4f;
	    g2d.dispose();
	    img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	    g2d = img.createGraphics();
	    prepare(g2d);
	    g2d.setFont(font);
	    fm = g2d.getFontMetrics();
	    g2d.setColor(Color.WHITE);
	    for(int i = 0; i < font.getNumGlyphs(); i++) {
	    	if(font.canDisplay(i)) {
	    		Glyph g = characterInfo.get(i);
	    		g.calculateTextureCoords(width, height);
	    		g.calculateVertices();
	    		g2d.drawString("" + (char) i, g.getX(), g.getY());
	    	}
	    }
	    g2d.dispose();
	    if(output != null)
		   try {
			   output.createNewFile();
			   ImageIO.write(img, "PNG", output);
		   } catch (IOException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		   }
	    return img;
	}
	
	public BufferedImage writeTexture(String text, File output) {
		BufferedImage img = initGraphics(text);
	    if(output != null)
		   try {
			   output.createNewFile();
			   ImageIO.write(img, "PNG", output);
		   } catch (IOException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		   }
	    textureOutput = output;
	    return img;
	}
	
	private void prepare(Graphics2D g2d) {
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
	    g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
	    g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
	}
	
	private BufferedImage initGraphics(String text) {
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = null;
		createGraphics(g2d, img);
		@SuppressWarnings("null")//createGraphics initializes the variable
		FontMetrics fm = g2d.getFontMetrics();
		int width = fm.stringWidth(text);
		int height = fm.getHeight();
		
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		createGraphics(g2d, img);
	    fm = g2d.getFontMetrics();
		prepare(g2d);
	    g2d.setColor(Color.WHITE);
	    g2d.drawString(text, 0, fm.getAscent());
	    g2d.dispose();
		return img;
	}
	
	private Graphics2D createGraphics(Graphics2D g2d, BufferedImage img) {
		g2d = img.createGraphics();
	    g2d.setFont(font);
	    return g2d;
	}
	
	public Texture getTexture() {
		return fontTexture;
	}

	public int getFontType() {
		return fontType;
	}

	public int getSize() {
		return size;
	}

}
