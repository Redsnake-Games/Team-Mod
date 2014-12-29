package dragon.Render;


import java.io.IOException;
import java.net.URL;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ARBTransposeMatrix;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

public class maintest
{
  private float view_rotx = 20.0F;
  private float view_roty = 30.0F;
  private float view_rotz = 0.0F;
  
  private boolean pause = false;
  
  private ObjectRenderer obj; 

  public static void main(String[] args)
  {
    new maintest().execute();
    System.exit(0);
  }

  private void execute()
  {
    try
    {
      init();
    

    loop();
} catch (Exception le) {
      le.printStackTrace();
      System.out.println("Failed to initialize Gears.");
      return;
    }
    destroy();
  }

  private void destroy()
  {
    Display.destroy();
  }

  private void loop() throws LWJGLException, Exception
  {
    long startTime = System.currentTimeMillis() + 5000L;
    long fps = 0L;
    long lastTime=0;
    
    AnimationManager.addAnimation(new AnimationWing(), obj);
    AnimationManager.addAnimation(new AnimationWalking(), obj);
    
    
    while (!Display.isCloseRequested()) 
    {
    	if(System.currentTimeMillis()-lastTime>=(1000D/50D))
    	{	
    		if(!pause)
    			AnimationManager.tick();
    		lastTime=System.currentTimeMillis();
    		
    	}
    	if(Display.isActive())
    	{
    		if(Keyboard.isKeyDown(Keyboard.KEY_O))
    			GL11.glScalef(0.99F, 0.99F, 0.99F);
    		if(Keyboard.isKeyDown(Keyboard.KEY_P))
    			GL11.glScalef(1.01F, 1.01F, 1.01F);
    		
    		while(Keyboard.next())
    		{
    			int key = Keyboard.getEventKey();
    			if(Keyboard.getEventKeyState())	
    			{
    				switch (key) 
    				{
    				case Keyboard.KEY_F1:
						view_rotx = 20;
						view_roty = 30;
						view_rotz = 0;
						break;

    				case Keyboard.KEY_F2:
						view_rotx = 0;
						view_roty = 0;
						view_rotz = 0;
						break;

    				case Keyboard.KEY_F3:
						view_rotx = 90;
						view_roty = 0;
						view_rotz = 0;
						break;
						
    				case Keyboard.KEY_F4:
						view_rotx = 0;
						view_roty = 90;
						view_rotz = 0;
						break;
					}
    			if(key == Keyboard.KEY_SPACE)	
    			{
    				pause = !pause;
    			}
    			}
    		}
    		
    		if(Mouse.isButtonDown(0))
    		{
    			GL11.glTranslatef(Mouse.getDX()*0.02F, 0, 0);
    			GL11.glTranslatef(0, Mouse.getDY()*0.02F, 0);
    		}

    	}
    	GL11.glClear(16640);
    	
    	GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    	GL11.glDisable(GL11.GL_DEPTH_TEST);
    	GL11.glColor4f(0.5F,0.5F,0.5F,1.0F);

		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glVertex2f(-100,-100);
		GL11.glVertex2f(Display.getWidth(),-100);
		GL11.glVertex2f(Display.getWidth(),Display.getHeight());
		GL11.glVertex2f(-100,Display.getHeight());
		
		GL11.glEnd();
		GL11.glColor4f(1.0F,1.0F,1.0F,1.0F);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
    	GL11.glPushMatrix();
    	
    	GL11.glTranslatef(-2.5F, -2.5F, 5);
    	
    	GL11.glRotatef(this.view_rotx, 1.0F, 0.0F, 0.0F);
    	GL11.glRotatef(this.view_roty, 0.0F, 1.0F, 0.0F);
    	GL11.glRotatef(this.view_rotz, 0.0F, 0.0F, 1.0F);
    	
    	
/*
    	GL11.glPushMatrix();
    	GL11.glTranslatef(-3.0F, -2.0F, 0.0F);
    	GL11.glRotatef(this.angle, 0.0F, 0.0F, 1.0F);
    	GL11.glCallList(this.gear1);
    	GL11.glPopMatrix();

    	GL11.glPushMatrix();
    	GL11.glTranslatef(3.1F, -2.0F, 0.0F);
      	GL11.glRotatef(-2.0F * this.angle - 9.0F, 0.0F, 0.0F, 1.0F);
      	GL11.glCallList(this.gear2);
      	GL11.glPopMatrix();

      	GL11.glPushMatrix();
      	GL11.glTranslatef(-3.1F, 4.2F, 0.0F);
      	GL11.glRotatef(-2.0F * this.angle - 25.0F, 0.0F, 0.0F, 1.0F);
      	GL11.glCallList(this.gear3);
      	GL11.glPopMatrix();
      	
      	GL11.glPushMatrix();
      	GL11.glTranslatef(5.5F, 2.5F, 0.0F);
      	GL11.glRotatef(1.5F * this.angle - 25.0F, 0.0F, 0.0F, 1.0F);
      	;
      	GL11.glPopMatrix();
*/		GL11.glPushMatrix();
		//obj.getObject("body").setRotation(0, angle, 0);
		/*
		AnimationBase ani = new AnimationWalking();
		max = ani.getNeededTicks();
		ani.render(obj, count);
		//body*/
		GL11.glPushMatrix();
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		obj.render();    	
		GL11.glPopMatrix();
		
		/**
    	GL11.glPushMatrix();
    	GL11.glTranslatef(0, 1, 0);
    	GL11.glRotated(Math.sin(this.angle)*30, 1.0F, 0.0F, 0.0F);
    	GL11.glTranslatef(0, -2, 0);
    	GL11.glScaled(0.5, 1, 0.5);   	
    	obj.render();    	
    	GL11.glPopMatrix();
    	
    	GL11.glPushMatrix();
    	GL11.glTranslatef(1, 1, 0);
    	GL11.glRotated(Math.sin(this.angle)*-30, 1.0F, 0.0F, 0.0F);
    	GL11.glTranslatef(0, -2, 0);
    	GL11.glScaled(0.5, 1, 0.5);   	
    	obj.render();    	
    	GL11.glPopMatrix();*/
    	
    	//Grid

    	GL11.glEnable(GL11.GL_TEXTURE_2D);
    	GL11.glColor4d(1, 1, 1, 1);
    //	GL11.glBindTexture(GL11.GL_TEXTURE_2D, TextureLoader.load("dirt.png", GL11.GL_NEAREST)[0]);
    	drawRect(-0.5, -1, -0.5, 0.5, 0, 0.5, 0, 0, 1, 1);
    	GL11.glDisable(GL11.GL_TEXTURE_2D);
    	
    	GL11.glColor4d(0, 0, 0, 1);
    	float fmin = -3.5F;
    	float fmax =  3.5F;
    	for(float i = fmin;i<=fmax;i++)
    	{
    		draw3Dline(fmin, 0, i, fmax, 0, i);
    		draw3Dline(i, 0, fmin, i, 0, fmax);
    	}

    	
    	/*
    	GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex3f(-1 ,-1,1);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3f(1, -1,1);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex3f(1, 1,1);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(-1 ,1,1);
		
		GL11.glEnd();
		*/
    	//drawRect2(-5, -2F, -5, 5, -1, 5);
    	GL11.glPopMatrix();

    	
      	GL11.glPopMatrix();

      	Display.update();
      	if (startTime > System.currentTimeMillis()) {
      		fps += 1L;
      	} else {
      		long timeUsed = 5000L + (startTime - System.currentTimeMillis());
      		startTime = System.currentTimeMillis() + 5000L;
      		System.out.println(fps + " frames in " + (float)timeUsed / 1000.0F + " seconds = " + (float)fps / ((float)timeUsed / 1000.0F));

      		fps = 0L;
      	}
        //Display.sync(45);
    }
  }

  private void init()
    throws LWJGLException
  {
    
    //DisplayMode[] d = Display.getAvailableDisplayModes();
    //Display.setDisplayModeAndFullscreen(d[0]);
    
    Display.setDisplayMode(new DisplayMode(700, 700));
    
    Display.setTitle("Gears");
    
    Display.create();

    FloatBuffer pos1 = BufferUtils.createFloatBuffer(4).put(new float[] { 5.0F, 5.0F, 10.0F, 0.0F });

    pos1.flip();
    
    GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, pos1);  
    GL11.glEnable(GL11.GL_LIGHT0);
   
    GL11.glEnable(GL11.GL_CULL_FACE);
    //GL11.glEnable(GL11.GL_LIGHTING);
    GL11.glEnable(GL11.GL_DEPTH_TEST);
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    GL11.glEnable(GL11.GL_ALPHA_TEST);
	
   try {
			obj = ObjectReader.readObj(new URL("res/teamFlag.obj"));			
		} catch (IOException e) {
			e.printStackTrace();
		}
    GL11.glEnable(GL11.GL_NORMALIZE);

    GL11.glMatrixMode(GL11.GL_PROJECTION);

    System.err.println("LWJGL: " + Sys.getVersion() + " / " + LWJGLUtil.getPlatformName());
    System.err.println("GL_VENDOR: " + GL11.glGetString(GL11.GL_VENDOR));
    System.err.println("GL_RENDERER: " + GL11.glGetString(GL11.GL_RENDERER));
    System.err.println("GL_VERSION: " + GL11.glGetString(GL11.GL_VERSION));
    System.err.println();
    System.err.println("glLoadTransposeMatrixfARB() supported: " + GLContext.getCapabilities().GL_ARB_transpose_matrix);
    if (!GLContext.getCapabilities().GL_ARB_transpose_matrix)
    {
      GL11.glLoadIdentity();
    }
    else {
      FloatBuffer identityTranspose = BufferUtils.createFloatBuffer(16).put(new float[] { 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F });

      identityTranspose.flip();
      ARBTransposeMatrix.glLoadTransposeMatrixARB(identityTranspose);
    }

    float h = 1.0F;
    GL11.glFrustum(-1.0D, 1.0D, -h, h, 5.0D, 60.0D);
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    GL11.glLoadIdentity();
    GL11.glTranslatef(0.0F, 0.0F, -40.0F);
  }
  
  private void qube(float l)
  {	  
	  
	  //GL11.glColor4f(1, 0, 0, 1);
	  
	  GL11.glBegin(GL11.GL_QUADS);
	  GL11.glVertex3d(-l,-l, l);
	  GL11.glVertex3d( l,-l, l);
	  GL11.glVertex3d( l, l, l);
	  GL11.glVertex3d(-l, l, l);
	  GL11.glEnd();
	  
	  GL11.glRotatef(180.0F, 1, 0, 0);
	  GL11.glBegin(GL11.GL_QUADS);
	  GL11.glVertex3d(-l,-l, l);
	  GL11.glVertex3d( l,-l, l);
	  GL11.glVertex3d( l, l, l);
	  GL11.glVertex3d(-l, l, l);
	  GL11.glEnd();
	  
	  //GL11.glColor4f(0, 1, 0, 1);
	  
	  GL11.glRotatef(90.0F, 1, 0, 0);
	  GL11.glBegin(GL11.GL_QUADS);
	  GL11.glVertex3d(-l,-l, l);
	  GL11.glVertex3d( l,-l, l);
	  GL11.glVertex3d( l, l, l);
	  GL11.glVertex3d(-l, l, l);
	  GL11.glEnd();
	  
	  GL11.glRotatef(180.0F, 1, 0, 0);
	  GL11.glBegin(GL11.GL_QUADS);
	  GL11.glVertex3d(-l,-l, l);
	  GL11.glVertex3d( l,-l, l);
	  GL11.glVertex3d( l, l, l);
	  GL11.glVertex3d(-l, l, l);
	  GL11.glEnd();
	  
	  //GL11.glColor4f(0, 0, 1, 1);
	  
	  GL11.glRotatef(90F, 0, 1, 0);
	  GL11.glBegin(GL11.GL_QUADS);
	  GL11.glVertex3d(-l,-l, l);
	  GL11.glVertex3d( l,-l, l);
	  GL11.glVertex3d( l, l, l);
	  GL11.glVertex3d(-l, l, l);
	  GL11.glEnd();
	  
	  GL11.glRotatef(180.0F, 0, 1, 0);
	  GL11.glBegin(GL11.GL_QUADS);
	  GL11.glVertex3d(-l,-l, l);
	  GL11.glVertex3d( l,-l, l);
	  GL11.glVertex3d( l, l, l);
	  GL11.glVertex3d(-l, l, l);
	  GL11.glEnd();
	  
	  //drawRect(-l, -l, -l, l, l, l);
  }

  private void drawRect(double x1,double y1,double z1,double x2,double y2,double z2,float p1,float p2,float p3,float p4)
  {	  
	  GL11.glShadeModel(GL11.GL_FLAT);
	  
	  
	  GL11.glBegin(GL11.GL_QUADS);
	  
	  //GL11.glColor4f(1, 0, 0, 1);
	  
	  GL11.glTexCoord2f(p1, p4);
	  GL11.glVertex3d(x1, y2, z1);
	  GL11.glTexCoord2f(p3, p4);
	  GL11.glVertex3d(x2, y2, z1);
	  GL11.glTexCoord2f(p3, p2);
	  GL11.glVertex3d(x2, y1, z1);
	  GL11.glTexCoord2f(p1, p2);
	  GL11.glVertex3d(x1, y1, z1);
	  
	  GL11.glTexCoord2f(p1, p4);
	  GL11.glVertex3d(x2, y2, z2);
	  GL11.glTexCoord2f(p3, p4);
	  GL11.glVertex3d(x1, y2, z2);
	  GL11.glTexCoord2f(p3, p2);
	  GL11.glVertex3d(x1, y1, z2);
	  GL11.glTexCoord2f(p1, p2);
	  GL11.glVertex3d(x2, y1, z2);
	  
	  //GL11.glColor4f(0, 1, 0, 1);
	 
	  GL11.glTexCoord2f(p1, p4);
	  GL11.glVertex3d(x2, y1, z1);
	  GL11.glTexCoord2f(p3, p4);
	  GL11.glVertex3d(x2, y2, z1);
	  GL11.glTexCoord2f(p3, p2);
	  GL11.glVertex3d(x2, y2, z2);
	  GL11.glTexCoord2f(p1, p2);
	  GL11.glVertex3d(x2, y1, z2);
	  
	  GL11.glTexCoord2f(p1, p4);
	  GL11.glVertex3d(x1, y1, z2);
	  GL11.glTexCoord2f(p3, p4);
	  GL11.glVertex3d(x1, y2, z2);
	  GL11.glTexCoord2f(p3, p2);
	  GL11.glVertex3d(x1, y2, z1);
	  GL11.glTexCoord2f(p1, p2);
	  GL11.glVertex3d(x1, y1, z1);
	  
	  //GL11.glColor4f(0, 0, 1, 1);
	
	  GL11.glTexCoord2f(p1, p4);
	  GL11.glVertex3d(x1, y1, z1);
	  GL11.glTexCoord2f(p3, p4);
	  GL11.glVertex3d(x2, y1, z1);
	  GL11.glTexCoord2f(p3, p2);
	  GL11.glVertex3d(x2, y1, z2);
	  GL11.glTexCoord2f(p1, p2);
	  GL11.glVertex3d(x1, y1, z2);

	  GL11.glTexCoord2f(p1, p4);
	  GL11.glVertex3d(x1, y2, z2);
	  GL11.glTexCoord2f(p3, p4);
	  GL11.glVertex3d(x2, y2, z2);
	  GL11.glTexCoord2f(p3, p2);
	  GL11.glVertex3d(x2, y2, z1);
	  GL11.glTexCoord2f(p1, p2);
	  GL11.glVertex3d(x1, y2, z1);
	  
	  GL11.glEnd();
	  
	  GL11.glShadeModel(GL11.GL_SMOOTH);
  }

  private void drawWiredRect(double x1,double y1,double z1,double x2,double y2,double z2)
  {
	 draw3Dline(x1, y1, z1, x2, y1, z1);
	 draw3Dline(x1, y2, z1, x2, y2, z1);
	 draw3Dline(x1, y1, z2, x2, y1, z2);
	 draw3Dline(x1, y2, z2, x2, y2, z2);
	 
	 draw3Dline(x1, y1, z1, x1, y2, z1);
	 draw3Dline(x2, y1, z1, x2, y2, z1);
	 draw3Dline(x1, y1, z2, x1, y2, z2);
	 draw3Dline(x2, y1, z2, x2, y2, z2);
	 
	 draw3Dline(x1, y1, z1, x1, y1, z2);
	 draw3Dline(x1, y2, z1, x1, y2, z2);
	 draw3Dline(x2, y1, z1, x2, y1, z2);
	 draw3Dline(x2, y2, z1, x2, y2, z2);
	 
  }

  private void drawRect2(float x1,float y1,float z1,float x2,float y2,float z2)
  {	
	  GL11.glBegin(GL11.GL_QUADS);
	  
	  GL11.glVertex3d(x1, y1, z1);
	  GL11.glVertex3d(x2, y1, z1);
	  GL11.glVertex3d(x2, y2, z1);
	  GL11.glVertex3d(x1, y2, z1);
	  GL11.glEnd();
	  
	  GL11.glRotatef(180.0F, 1, 0, 0);
	  GL11.glBegin(GL11.GL_QUADS);
	  GL11.glVertex3d(x1, y1, -z2);
	  GL11.glVertex3d(x2, y1, -z2);
	  GL11.glVertex3d(x2, y2, -z2);
	  GL11.glVertex3d(x1, y2, -z2);
	  GL11.glEnd();
	  
	  //GL11.glColor4f(0, 1, 0, 1);
	  /*
	  GL11.glRotatef(90.0F, 1, 0, 0);

	  GL11.glVertex3d(-l,-l, l);
	  GL11.glVertex3d( l,-l, l);
	  GL11.glVertex3d( l, l, l);
	  GL11.glVertex3d(-l, l, l);

	  
	  GL11.glRotatef(180.0F, 1, 0, 0);
	  GL11.glBegin(GL11.GL_QUADS);
	  GL11.glVertex3d(-l,-l, l);
	  GL11.glVertex3d( l,-l, l);
	  GL11.glVertex3d( l, l, l);
	  GL11.glVertex3d(-l, l, l);

	  
	  //GL11.glColor4f(0, 0, 1, 1);
	  
	  GL11.glRotatef(90F, 0, 1, 0);

	  GL11.glVertex3d(-l,-l, l);
	  GL11.glVertex3d( l,-l, l);
	  GL11.glVertex3d( l, l, l);
	  GL11.glVertex3d(-l, l, l);

	  
	  GL11.glRotatef(180.0F, 0, 1, 0);

	  GL11.glVertex3d(-l,-l, l);
	  GL11.glVertex3d( l,-l, l);
	  GL11.glVertex3d( l, l, l);
	  GL11.glVertex3d(-l, l, l);*/
	 // GL11.glEnd();
	  
  }
  
  /*
  public void render3D(String pic,double b) throws IOException
  {
	  int[] id = TextureLoader.load(pic, GL11.GL_NEAREST);
	  int tex = id[0];
	  float w = id[1];
	  float h = id[2];
	  //BufferedImage buf = ImageIO.read(TextureLoader.class.getResourceAsStream(pic));
	  GL11.glRotatef(180, 1, 0, 0);
	  
	  GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
	  
	  for(float i1=0;i1<w;i1+=1)
	  {
		  for(float i2=0;i2<h;i2+=1)
		  {			  
			  if(buf.getAlphaRaster().getPixel((int)i1, (int)i2, new int[1])[0]==0)
				  continue;
			  
			  float p1 = i1/w;
			  float p2 = i2/h;
			  float p3 = (i1+1)/w;
			  float p4 = (i2+1)/h;
			  
			  drawRect((i1)*b, (i2)*b, 0, (i1+1)*b, (i2+1)*b, b, p1, p2, p3, p4);
		  }
	  }	  
  }
  */
  public void draw3Dline(double x1,double y1,double z1,double x2,double y2,double z2)
  {
	  GL11.glBegin(GL11.GL_LINES);
	  
	  GL11.glVertex3d(x1, y1, z1);
	  GL11.glVertex3d(x2, y2, z2);
	  
	  GL11.glEnd();
  }
}

