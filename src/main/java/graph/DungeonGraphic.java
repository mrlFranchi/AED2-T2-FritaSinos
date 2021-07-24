package graph;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DungeonGraphic extends JFrame
{
    AbstractGraph graph;
    List<Vertex> traversalPath;
    public DungeonGraphic(AbstractGraph graph, @Nullable List<Vertex> traversalPath)
    {
        super("Dungeon");
        this.graph = graph;
        this.traversalPath = traversalPath;
        getContentPane().setBackground(Color.black);
        setSize(900, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.repaint();
    }

    public DungeonGraphic(String title, AbstractGraph graph, @Nullable List<Vertex> traversalPath){
        super(title);
        this.graph = graph;
        this.traversalPath = traversalPath;
        getContentPane().setBackground(Color.black);
        setSize(900, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.repaint();
    }

    private void drawDungeon(Graphics g)
    {
        Graphics2D graphics2D = (Graphics2D) g;
        if(graph != null)
        {
            for (int i = 0; i < graph.getNumberOfVertices(); i++)
            {
                String id = "";
                graphics2D.setColor(Color.WHITE);
                Vertex currentVertex = graph.getVertices().get(i);
                graphics2D.draw(((Room)currentVertex).getRoom());
                if(((Room)currentVertex).isCheckpoint())
                {
                    graphics2D.setColor(Color.YELLOW);
                    graphics2D.fill(((Room) currentVertex).getRoom());
                }
                else if(((Room)currentVertex).isExit())
                {
                    graphics2D.setColor(Color.RED);
                    graphics2D.fill(((Room) currentVertex).getRoom());
                }
                else if(((Room)currentVertex).isEntrance())
                {
                    graphics2D.setColor(Color.GREEN);
                    graphics2D.fill(((Room) currentVertex).getRoom());
                }
                if(((Room) currentVertex).getKeyID() > -1)
                {
                    graphics2D.setColor(Color.MAGENTA);
                    graphics2D.draw(((Room) currentVertex).getRoom());
                    Point pt = ((Room) currentVertex).getPoint();
                    id = id+"K"+((Room) currentVertex).getKeyID();
                }
                Point pt = ((Room) currentVertex).getPoint();
                g.drawString(""+i+" "+id, pt.x, pt.y-2);
                Vertex adjacentVertex = graph.getFirstConnectedVertex(currentVertex);
                while(adjacentVertex != null)
                {
                    g.setColor(Color.PINK);
                    if(traversalPath != null)
                    {
                        if(traversalPath.contains(adjacentVertex) && traversalPath.contains(currentVertex))
                        {
                            g.setColor(Color.CYAN);
                        }
                    }
                    if(graph.getEdge(currentVertex, adjacentVertex).getLockID() > -1)
                    {
                        g.setColor(Color.GREEN);
                        g.drawString("L"+graph.getEdge(currentVertex, adjacentVertex).getLockID(),
                                (int)Math.abs((((Room) currentVertex).getPoint().getX() + ((Room) adjacentVertex).getPoint().getX())/2),
                                (int)Math.abs((((Room) currentVertex).getPoint().getY() + ((Room) adjacentVertex).getPoint().getY())/2));
                    }
                    g.drawLine((int)((Room) currentVertex).getPoint().getX(), (int)((Room) currentVertex).getPoint().getY(),
                            (int)((Room) adjacentVertex).getPoint().getX(),
                            (int) ((Room) adjacentVertex).getPoint().getY());
                    adjacentVertex = graph.getNextConnectedVertex(currentVertex, adjacentVertex);
                }
            }
        }
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        drawDungeon(g);
    }
}
