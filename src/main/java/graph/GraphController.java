package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public final class GraphController
{
    private static final Logger LOGGER = Logger.getLogger("GraphController.class");

    private AbstractGraph g;
    private TraversalStrategyInterface traversalStrategy;
    private final List<Vertex> vertices;

    private GraphController()
    {
        vertices = createVertexList();
    }

    public static void main(String[] args)
    {
        GraphController graphController = new GraphController();

        graphController.g = new DigraphMatrix(graphController.vertices);
        graphController.test("MatrixDigraph");

        graphController.g = new GraphMatrix(graphController.vertices);
        graphController.test("MatrixGraph");

        graphController.g = new DigraphList(graphController.vertices);
        graphController.test("ListDigraph");

        graphController.g = new GraphList(graphController.vertices);
        graphController.test("ListGraph");

        graphController.g = new DigraphMap(graphController.vertices);
        graphController.test("MapDigraph");

        graphController.g = new GraphMap(graphController.vertices);
        graphController.test("MapGraph");

        //Main Ex6
        GraphController dungeonController = new GraphController();
        dungeonController.dungeon = new DigraphList();
        dungeonController.testDungeon("ListDigraph");
    }

    private static List<Vertex> createVertexList()
    {
        List<Vertex> vertices = new ArrayList<>();
        vertices.add(new Vertex("Joao"));
        vertices.add(new Vertex("Maria"));
        vertices.add(new Vertex("José"));
        vertices.add(new Vertex("Marcos"));
        vertices.add(new Vertex("Pedro"));
        vertices.add(new Vertex("Amanda"));
        vertices.add(new Vertex("Leonardo"));
        return vertices;
    }

    private void test(String fileName)
    {
        createTest();
        printTest(fileName);
    }

    private void printTest(String fileName)
    {
        String graphString = "\n"+ g +"\n";
        LOGGER.info(graphString);
        traversalStrategy.traverseGraph(g.getVertices().get(0), null);
        if(traversalStrategy instanceof FloydWarshallTraversal)
        {
            Vertex center = g.getCentermostVertex(((FloydWarshallTraversal)traversalStrategy).getDistanceMatrix());
            String centerVertexString = "\n"+ center +"\n";
            LOGGER.info(centerVertexString);
        }
    }

    private void createTest()
    {
        g.addEdge(g.getVertices().get(0), g.getVertices().get(1), 2);
        g.addEdge(g.getVertices().get(0), g.getVertices().get(2), 5);
        g.addEdge(g.getVertices().get(1), g.getVertices().get(2), 2);
        g.addEdge(g.getVertices().get(2), g.getVertices().get(0), 3);
        g.addEdge(g.getVertices().get(2), g.getVertices().get(3), 7);
        g.addEdge(g.getVertices().get(3), g.getVertices().get(4), 6);
        g.addEdge(g.getVertices().get(4), g.getVertices().get(5), 2);
        g.addEdge(g.getVertices().get(5), g.getVertices().get(0), 3);
        g.addEdge(g.getVertices().get(0), g.getVertices().get(6), 10);

        traversalStrategy = new PrimMSTTraversal(g);
    }

    private void testDungeon(String fileName){
        Scanner read = new Scanner(System.in);
        int seed = read.nextInt();
        int nRooms = read.nextInt();
        RandomSingleton.getInstance(seed); //Inicializar gerador de números aleatórios com a semente fornecida pelo usuário
        RandomDungeonGenerator randomDungeonGenerator = new RandomDungeonGenerator(nRooms); //Gerar n salas
        dungeon = randomDungeonGenerator.getDungeon(); //Pegar o grafo com salas
        DelaunayTriangulation.triangulateGraphVertices(dungeon); //Criar arestas
//        SwingUtilities.invokeLater(() -> new DungeonGraphic(dungeonController.dungeon).setVisible(true)); //Imprime a Dungeon

        traversalStrategy = new PrimMSTTraversal(dungeon);
        traversalStrategy.traverseGraph(dungeon.getVertices().get(0));

        //Pegar a lista de vértices predecessores criada na execução do algoritmo de Prim
        int[] predecessorIndexList = traversalStrategy.getPredecessorArray();
        //Converter a lista para uma nova dungeon
        dungeon = convertPredecessorListToGraph(dungeon, predecessorIndexList);
//        SwingUtilities.invokeLater(() -> new DungeonGraphic(dungeon).setVisible(true)); //Imprime a Dungeon

        traversalStrategy = new BreadthFirstTraversal(dungeon);
        traversalStrategy.traverseGraph(dungeon.getVertices().get(0));
//        SwingUtilities.invokeLater(() -> new DungeonGraphic(dungeon).setVisible(true)); //Imprime a Dungeon
    }
}
