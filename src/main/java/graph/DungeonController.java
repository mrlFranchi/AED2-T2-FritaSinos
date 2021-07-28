package graph;

import javax.swing.*;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class DungeonController
{
    private static final Logger LOGGER = Logger.getLogger("DungeonController.class");

    private AbstractGraph dungeon;

    private static final int TOTAL_LOCKS = 3;
    private Room entrance;
    private Room exit;

    private DungeonController()
    {
    }

    public static void main(String[] args)
    {
        DungeonController dungeonController = new DungeonController();
        createRandomDungeon(dungeonController);
        DelaunayTriangulation.triangulateGraphVertices(dungeonController.dungeon);
        CreateDungeonGraphic(dungeonController,"DelauneyTriangulation");
        ReplaceDungeonWithMST(dungeonController);
        CreateDungeonGraphic(dungeonController,"MST");
        setSpecialRooms(dungeonController);
        List<Vertex> traversalPath = getPathFromEntranceToExit(dungeonController);
        CreateDungeonGraphic(dungeonController, traversalPath,"Special Rooms");
        setLocksAndKeys(dungeonController);
        CreateDungeonGraphic(dungeonController, traversalPath,"Keys And Locks");

        TraversalStrategyInterface BFT = new BreadthFirstTraversal(dungeonController.dungeon);
        BFT.traverseGraph(dungeonController.entrance, dungeonController.exit);
        TraversalStrategyInterface DFT = new DepthFirstTraversal(dungeonController.dungeon);
        DFT.traverseGraph(dungeonController.entrance, dungeonController.exit);
        TraversalStrategyInterface aStar = new AStartPathFind(dungeonController.dungeon);
        aStar.traverseGraph(dungeonController.entrance, dungeonController.exit);


    }

    private static void CreateDungeonGraphic(DungeonController dungeonController)
    {
        SwingUtilities.invokeLater(() -> new DungeonGraphic(dungeonController.dungeon, null).setVisible(true));
    }

    private static void CreateDungeonGraphic(DungeonController dungeonController, List<Vertex> traversalPath)
    {
        SwingUtilities.invokeLater(() -> new DungeonGraphic(dungeonController.dungeon, traversalPath).setVisible(true));
    }

    private static void CreateDungeonGraphic(DungeonController dungeonController, String title)
    {
        SwingUtilities.invokeLater(() -> new DungeonGraphic(title, dungeonController.dungeon, null).setVisible(true));
    }
    private static void CreateDungeonGraphic(DungeonController dungeonController, List<Vertex> traversalPath,String title)
    {
        SwingUtilities.invokeLater(() -> new DungeonGraphic(title, dungeonController.dungeon, traversalPath).setVisible(true));
    }

    private static void createRandomDungeon(DungeonController dungeonController)
    {
        System.out.println("What will be the random seed?");
        Scanner scanner = new Scanner(System.in);
        int seed = Integer.parseInt(scanner.nextLine());
        RandomSingleton.getInstance(seed);
        System.out.println("How many rooms will the dungeon have?");
        int nRooms = Integer.parseInt(scanner.nextLine());
        RandomDungeonGenerator randomDungeonGenerator = new RandomDungeonGenerator(nRooms);
        dungeonController.dungeon = randomDungeonGenerator.getDungeon();
    }

    private static void ReplaceDungeonWithMST(DungeonController dungeonController)
    {
        AbstractGraph dungeon = dungeonController.dungeon;
        TraversalStrategyInterface traversalStrategy;
        traversalStrategy = new PrimMSTTraversal(dungeon);
        traversalStrategy.traverseGraph(dungeon.getVertices().get(0),null);
        dungeonController.dungeon = GraphConverter.predecessorListToGraph(dungeon, traversalStrategy.getPredecessorArray());
    }

    private static void setSpecialRooms(DungeonController dungeonController)
    {
        AbstractGraph dungeon = dungeonController.dungeon;
        FloydWarshallTraversal traversalStrategy = new FloydWarshallTraversal(dungeon);
        traversalStrategy.traverseGraph(dungeon.getVertices().get(0), null);
        Room center = (Room) dungeon.getCentermostVertex(traversalStrategy.getDistanceMatrix());
        center.setCheckpoint(true);
        Room entrance = (Room) dungeon.getOuterMostVertex(traversalStrategy.getDistanceMatrix());
        entrance.setEntrance(true);
        dungeonController.entrance = entrance;
        Room exit = (Room) dungeon.getMostDistantVertex(traversalStrategy.getDistanceMatrix(), entrance);
        exit.setExit(true);
        dungeonController.exit = exit;
    }

    private static List<Vertex> getPathFromEntranceToExit(DungeonController dungeonController)
    {
        AbstractGraph dungeon = dungeonController.dungeon;
        TraversalStrategyInterface aStar = new AStartPathFind(dungeon);
        aStar.traverseGraph(dungeonController.entrance, dungeonController.exit);
        return aStar.getShortestPath(dungeonController.entrance, dungeonController.exit);
    }

    private static void setLocksAndKeys(DungeonController dungeonController)
    {
        AbstractGraph dungeon = dungeonController.dungeon;
        TraversalStrategyInterface traversalStrategy;
        traversalStrategy = new KeyLockGenerator(dungeon, TOTAL_LOCKS);
        traversalStrategy.traverseGraph(dungeonController.entrance, null);
    }
}
