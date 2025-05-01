package UI;

import Mechanics.*;
import Model.*;
import Utilities.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/** Swing front-end for our Texas Hold’em engine. */
public class GameUI extends JFrame {

    /* ─── layout constants ───────────────────────── */
    private static final int FW = 1920, FH = 1080;       // frame size
    private static final int CARD_W = 80, CARD_H = 120;  // sprite size
    private static final int BTN_W = 100, BTN_H = 100;
    /* ─────────────────────────────────────────────── */

    /* containers */
    private final JLayeredPane gameLayer = new JLayeredPane();
    private final JPanel buttonBar  = new JPanel(new FlowLayout());
    private final JPanel headerBar  = new JPanel();
    private final JLabel status     = new JLabel("Waiting for Game to Start");

    /* bg + icons */
    private final ImagePanel floor  = new ImagePanel("/assets/Floor.jpg",0,0,FW,FH);
    private final ImagePanel table  = new ImagePanel("/assets/Table.png",60,-100,FW-525,FH-250);
    private final ImagePanel[] cpu  = new ImagePanel[4];

    /* community + seats */
    private JPanel community;
    private final JPanel[] seat = new JPanel[5];
    private final JPanel[] slots= new JPanel[5];
    private final JLabel[] name = new JLabel[5];
    private final JLabel[] chip = new JLabel[5];

    /* seat origin for resizing */
    private final Point[] seatPos = new Point[5];
    private final Dimension[] seatDim = new Dimension[5];

    /* engine */
    private PokerGame poker;
    private Player current;

    /* action buttons */
    private JButton callB,checkB,foldB,raiseB,allInB;

    /* ─────────────────────────────────────────────── */
    public GameUI() {
        super("Texas Hold ’Em");
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.GREEN.darker());

        /* layer */
        gameLayer.setLayout(null);
        add(gameLayer,BorderLayout.CENTER);

        /* header */
        headerBar.setOpaque(false);
        headerBar.setLayout(new BoxLayout(headerBar,BoxLayout.Y_AXIS));
        JLabel ttl=new JLabel("Texas Hold ’Em");
        ttl.setFont(new Font("Arial",Font.BOLD,24));
        ttl.setAlignmentX(CENTER_ALIGNMENT);
        status.setAlignmentX(CENTER_ALIGNMENT);
        headerBar.add(ttl); headerBar.add(status);
        add(headerBar,BorderLayout.NORTH);

        /* button bar */
        buttonBar.setOpaque(false);
        add(buttonBar,BorderLayout.SOUTH);

        /* bg images */
        floor.setBounds(0,0,FW,FH);
        table.setBounds(0,0,FW,FH);
        gameLayer.add(floor,Integer.valueOf(0));
        gameLayer.add(table,Integer.valueOf(1));

        int[][] pos={{25,0},{25,450},{1300,450},{1300,0}};
        for(int i=0;i<4;i++){
            cpu[i]=new ImagePanel("/assets/UserIcon.png",100,100,200,200);
            cpu[i].setPosition(pos[i][0],pos[i][1]);
            cpu[i].setBounds(0,0,FW,FH);
            gameLayer.add(cpu[i],Integer.valueOf(2));
        }

        initCardAreas();
        saveSeatBounds();
        buildStartMenu();

        addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent e){ rescale(); }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(FW,FH);
        setVisible(true);
    }

    /* ───── card & seat panels ────────────────────── */
    private void initCardAreas(){
        /* community */
        community = new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));
        community.setOpaque(false);
        community.setBounds(FW/2-250,FH/2-60,500,120);
        gameLayer.add(community,Integer.valueOf(3));

        /* 5 seats */
        int[][] seatXY={{FW/2-100,FH-380},{225,750},{225,50},{1460,50},{1460,750}};
        for(int i=0;i<5;i++){
            seat[i]=new JPanel();
            seat[i].setLayout(new BoxLayout(seat[i],BoxLayout.Y_AXIS));
            seat[i].setOpaque(false);
            seat[i].setBounds(seatXY[i][0],seatXY[i][1],200,CARD_H+40);

            /* info */
            name[i]=new JLabel("Player "+(i+1));
            name[i].setForeground(Color.WHITE);
            name[i].setFont(new Font("Arial",Font.BOLD,14));
            chip[i]=new JLabel("$0"); chip[i].setForeground(Color.YELLOW);
            chip[i].setFont(new Font("Arial",Font.BOLD,14));

            JPanel info=new JPanel();
            info.setLayout(new BoxLayout(info,BoxLayout.Y_AXIS));
            info.setOpaque(false);
            info.add(name[i]); info.add(chip[i]);

            /* slots (cards) */
            slots[i]=new JPanel(new FlowLayout(FlowLayout.CENTER,5,0));
            slots[i].setOpaque(false);
            slots[i].setPreferredSize(new Dimension(CARD_W*2+15,CARD_H));
            /* size-lock so BoxLayout never shrinks -> prevents wrap */
            Dimension pref=slots[i].getPreferredSize();
            slots[i].setMinimumSize(pref);
            slots[i].setMaximumSize(pref);

            seat[i].add(info);
            seat[i].add(slots[i]);
            gameLayer.add(seat[i],Integer.valueOf(3));
        }
    }
    private void saveSeatBounds(){
        for(int i=0;i<5;i++){
            seatPos[i]=seat[i].getLocation();
            seatDim[i]=seat[i].getSize();
        }
    }

    /* ───── start-menu buttons ────────────────────── */
    private void buildStartMenu(){
        JButton start=new JButton("Start New Game");
        start.setPreferredSize(new Dimension(BTN_W*2,BTN_H));
        buttonBar.add(start);
        repaintLayer();

        JButton easy=new JButton("Easy"),
                med =new JButton("Medium"),
                hard=new JButton("Hard");
        easy.setPreferredSize(btnSize()); med.setPreferredSize(btnSize());
        hard.setPreferredSize(btnSize());

        start.addActionListener(e->{
            buttonBar.removeAll(); status.setText("Select difficulty");
            buttonBar.add(easy); buttonBar.add(med); buttonBar.add(hard);
            repaintLayer();
        });
        easy.addActionListener(e->startGame(10000));
        med .addActionListener(e->startGame( 5000));
        hard.addActionListener(e->startGame( 1000));
    }
    private Dimension btnSize(){return new Dimension(BTN_W,BTN_H);}

    /* ───── action bar ────────────────────────────── */
    private void buildActionBar(){
        callB =mk("Call");   checkB=mk("Check"); foldB=mk("Fold");
        raiseB=mk("Raise");  allInB=mk("All In");

        buttonBar.removeAll();
        buttonBar.add(callB);buttonBar.add(checkB);buttonBar.add(foldB);
        buttonBar.add(raiseB);buttonBar.add(allInB);
        repaintLayer();

        callB .addActionListener(e->click("call"));
        checkB.addActionListener(e->click("check"));
        foldB .addActionListener(e->click("fold"));
        raiseB.addActionListener(e->{
            String s=JOptionPane.showInputDialog("Raise amount:");
            try{ poker.gameActionRaising("raise",Integer.parseInt(s)); refresh();}
            catch(Exception ignored){}
        });
        allInB.addActionListener(e->click("allin"));
    }
    private JButton mk(String t){
        JButton b=new JButton(t);
        b.setPreferredSize(btnSize()); return b;
    }
    private void click(String a){ if(current!=null && current.isHuman()){
        poker.gameAction(a); refresh(); } }

    /* ───── begin game ────────────────────────────── */
    private void startGame(int stack){
        List<Player> ps=new ArrayList<>();
        String me=JOptionPane.showInputDialog("Your name:");
        if(me==null||me.trim().isEmpty()) me="Player";
        ps.add(new Player(me.trim(),stack,true));
        for(int i=1;i<5;i++) ps.add(new AutoPlayer("Bot "+i,stack,false));

        poker=new PokerGame(ps); poker.startGame();
        current=poker.getCurrentPlayer();

        buildActionBar(); refresh();
    }

    /* ───── refresh after each move / phase ───────── */
    private void refresh(){
        current=poker.getCurrentPlayer(); updateCards(); updateButtons();

        while(current!=null && !current.isHuman()){ // AI chain
            String act=((AutoPlayer)current).decideAction();
            poker.gameAction(act.toLowerCase());
            current=poker.getCurrentPlayer();
            updateCards();
        }
        updateButtons();
    }

    /* card & stack display */
    private void updateCards(){
        if(poker==null) return;

        community.removeAll();
        for(Card c:poker.getCommunityCards()) community.add(cardFace(c));

        List<Player> ps=poker.getPlayers();
        for(int i=0;i<ps.size();i++){
            Player p=ps.get(i);
            name[i].setText(p.getName()); chip[i].setText("$"+p.getMoney());
            slots[i].removeAll();
            for(Card c:p.getHand().getCards()){
                boolean show=p.isHuman()||poker.isShowdown();
                slots[i].add(show?cardFace(c):cardBack());
            }
        }
        repaintLayer();
    }
    private JLabel cardFace(Card c){
        String key=(c.getRank()+" of "+c.getSuit()).toUpperCase();
        return icon(Constants.CARD_IMAGES.get(key));
    }
    private JLabel cardBack(){ return icon(Constants.CARD_IMAGES.get("BACK")); }
    private JLabel icon(BufferedImage img){
        Image scaled=img.getScaledInstance(CARD_W,CARD_H,Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaled));
    }

    /* enable / disable */
    private void updateButtons(){
        boolean on=current!=null && current.isHuman();
        callB.setEnabled(on); checkB.setEnabled(on); foldB.setEnabled(on);
        raiseB.setEnabled(on); allInB.setEnabled(on);
    }

    /* ───── resize helper ─────────────────────────── */
    private void rescale(){
        int w=getWidth(), h=getHeight();
        for(int i=0;i<5;i++){
            seat[i].setBounds((int)(seatPos[i].x*w/(double)FW),
                              (int)(seatPos[i].y*h/(double)FH),
                              (int)(seatDim[i].width*w/(double)FW),
                              (int)(seatDim[i].height*h/(double)FH));
        }
        community.setBounds((int)((FW/2-250)*w/(double)FW),
                            (int)((FH/2-60) *h/(double)FH),
                            (int)(500*w/(double)FW),
                            (int)(120*h/(double)FH));
        floor.updateScale(w,h); table.updateScale(w,h);
        for(ImagePanel ip:cpu) ip.updateScale(w,h);
        repaintLayer();
    }

    private void repaintLayer(){ gameLayer.revalidate(); gameLayer.repaint(); }

    /* entry */
    public static void main(String[] a){ SwingUtilities.invokeLater(GameUI::new); }
}
