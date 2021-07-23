package graph;

public class GraphConverter {

    /* TODO: This class*/
    public static AbstractGraph predecessorListToGraph(AbstractGraph dungeon, int[] predecessor){
        AbstractGraph newDungeon = new DigraphList();
        //Adicionar os vértices da lista original
        Room newRoom;
        for (int i = 0; i < dungeon.getNumberOfVertices(); i++) {
            newRoom = (Room)dungeon.getVertices().get(i);
            newDungeon.addVertex(newRoom);
        }
        //Adiciona só as arestas contidas na lista de predecessores
        float distance;
        Room source, destination;
        for (int i = 1; i < predecessor.length; i++) {
            source = (Room)dungeon.getVertices().get(predecessor[i]);
            destination = (Room)dungeon.getVertices().get(i);
            distance = dungeon.calcDistance(source, destination);
            newDungeon.addEdge(source, destination, distance);
        }
        return newDungeon;
    }
}
