package com.mycompany.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

//Importa��o do
import com.mycompany.entities.Entity;
import com.mycompany.entities.Player;
import com.mycompany.Graficos.Spritesheet;
import com.mycompany.Graficos.UI;
import com.mycompany.entities.BulletShoot;
import com.mycompany.entities.Enemy;
import com.mycompany.world.World;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable,KeyListener, MouseListener{

	private static final long serialVersionUID = 1L;
	//Variables
	public static JFrame frame;
	public static boolean isRunning = true;
	public static Thread thread;
        public  static final int WIDTH = 240;
	public  static int HEIGHT = 160, SCALE = 3;
        private static int current_level = 1;
        private final int MAX_LEVEL = 2;
        public static GameState gameState = GameState.MENU;
	
	private BufferedImage image;
	
	public static List<Entity> entities;
        public static List<Enemy> enemies;
        public static List<BulletShoot> bulletsShoot;
	public static Spritesheet spritesheet;
	
	private Graphics g;
        public static Player player;
        
        public static World world;
        
        public static Random rand;
        
        public UI ui;
        
        public Menu menu;
	
	//Construtor
	public Game() {
            Sound.musicBackground.loop();
            rand = new Random();
            addKeyListener(this);
            addMouseListener(this);
            this.setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
            initFrame();
            //Inicializando bjetos
            ui = new UI();
            image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
            menu = new Menu();
	}
	
	//Cria��o da Janela
	public void initFrame() {
		frame = new JFrame("Zelda");
		frame.add(this);
		frame.setResizable(false);//Usu�rio n�o ir� ajustar janela
		frame.pack();
		frame.setLocationRelativeTo(null);//Janela inicializa no centro
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Fechar o programa por completo
		frame.setVisible(true);//Dizer que estar� vis�vel
	}
	
	//Threads
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public static synchronized void stop() {
            isRunning = false;
            thread.interrupt();            
	}
	
	public void tick() {
            if(gameState == GameState.NORMAL){
               for(int i = 0; i < entities.size(); i++) {
                    Entity e = entities.get(i);
                    if(e instanceof Player) {
                            //Ticks do Player
                    }
                    e.tick();
		}
                
                for(int i = 0; i < bulletsShoot.size(); i++){
                   bulletsShoot.get(i).tick();
                }
                
                if(enemies.size() == 0){
                   current_level++;
                   if(current_level > MAX_LEVEL){
                      current_level = 1; 
                   }
                   IniciarGame();
                } 
            }else if(gameState == GameState.GAME_OVER){
                
            }
            else if(gameState == GameState.MENU){
                menu.tick();
            }
		
	}
	
	//O que ser� mostrado em tela
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();//Sequ�ncia de buffer para otimizar a renderiza��o, lidando com performace gr�fica
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
                
                g = image.getGraphics();//Renderizar imagens na tela
                g.setColor(new Color(0, 0, 0));
                g.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
                
                g.dispose();//Limpar dados de imagem n�o usados
                g = bs.getDrawGraphics();
                g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE,null);
                
                if(gameState == GameState.NORMAL){                   
                    world.render(g);

                    for(int i = 0; i < entities.size(); i++) {
                            Entity e = entities.get(i);

                            e.render(g);
                    }

                    for(int i = 0; i < bulletsShoot.size(); i++){
                       bulletsShoot.get(i).render(g);
                    }

                    ui.render(g);

                    /*
                    g.dispose();//Limpar dados de imagem n�o usados
                    g = bs.getDrawGraphics();
                    g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE,null);
                    */

                    g.setFont(new Font("arial", Font.BOLD, 20));
                    g.setColor(Color.white);
                    g.drawString("Munição: " + player.getAmmo(), 580, 20); 
                }               
                else if(gameState == GameState.GAME_OVER){
                   Graphics2D g2 = (Graphics2D) g;
                   g2.setColor(new Color(0,0,0,100));
                   g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
                   
                   g.setFont(new Font("arial", Font.BOLD, 30));
                   g.setColor(Color.white);                   
                   g.drawString("Game OVER", (WIDTH*SCALE) / 2 - 110, (HEIGHT*SCALE) / 2);
                   g.drawString("Pressione ENTER para reiniciar", (WIDTH*SCALE) / 2 - 220, (HEIGHT*SCALE) / 2 + 110);
                }else if(gameState == GameState.MENU){
                    menu.render(g);
                }
                
		bs.show();
	}
	
	//Controle de FPS
	public void run() {
		//Variables
		long lastTime = System.nanoTime();//Usa o tempo atual do computador em nano segundos, bem mais preciso
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;//Calculo exato de Ticks
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
                
		//Ruuner Game
		while (isRunning == true) {
			//System.out.println("My game is Running");
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			if(delta >= 1) {
				tick(); 
                                render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: "+frames);
				frames = 0;
				timer += 1000;
			}
		}
		
		stop(); //Garante que todas as Threads relacionadas ao computador foram terminadas, para garantir performance.
		System.exit(1);
	}

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(gameState == GameState.NORMAL){
           if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D){
            player.right = true;  
            }else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A){
              player.left = true;  
            }

            if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
                player.up = true;
            }else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S){
                player.down = true;
            }

            if(e.getKeyCode() == KeyEvent.VK_X){
                player.setIsShooting(true);
            }  
            
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                menu.addNewMenuOptions("Continuar Jogo");
                gameState = GameState.MENU;
            }
            
        }else if(gameState == GameState.GAME_OVER){
           if(e.getKeyCode() == KeyEvent.VK_ENTER){
               Game.IniciarGame();
           } 
        }
        else if(gameState == GameState.MENU){
          if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
              menu.setUp(true);
          }else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S){
             menu.setDown(true); 
          }else if(e.getKeyCode() == KeyEvent.VK_ENTER){
             menu.setEnter(true); 
          }  
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
        if(gameState == GameState.NORMAL){
           if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D){
            player.right = false;  
           }else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A){
            player.left = false;  
           }
        
            if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
                player.up = false;
            }else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S){
                player.down = false;
            }  
        }
       
    }
    
    public static void IniciarGame(){
        if(entities != null){
          entities.clear();  
        }
        
        if(enemies != null){
          enemies.clear();  
        }
        
        if(bulletsShoot != null){
           bulletsShoot.clear(); 
        }
        
        
        entities = new ArrayList<Entity>();
        enemies = new ArrayList<Enemy>();
        bulletsShoot = new ArrayList<BulletShoot>();
        
	spritesheet = new Spritesheet("res/spritesheet.png");
	player = new Player(0,0,16,16,spritesheet.getSprite(32, 0, 16, 16));
        entities.add(player);
        String newWorld = "res/level"+Game.current_level+".png";
        world = new World(newWorld);
        
        gameState = GameState.NORMAL;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
       
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(gameState == GameState.NORMAL){
            player.setIsShooting(true); 
        } 
    }

    @Override
    public void mouseReleased(MouseEvent e) {
       
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
       
    }
	
}
