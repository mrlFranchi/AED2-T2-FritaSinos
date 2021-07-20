package graph;

import java.util.*;
/*
*   Classe principal, para o exercicio, cria um digrafo e faz a travessia com o metodo FloydWarshal.
* */
public class BrabaLog extends DigraphList{
    private DigraphList g;
    private FloydWarshallTraversal FWTraversal;
    private List<CityVertex> vertices;
    private BrabaLog(){
        vertices = new ArrayList<>();
        g = new DigraphList();
        FWTraversal = null;
    }

    public void addVertex(String name, double x, double y){
        CityVertex CV = new CityVertex(name,x,y);
        vertices.add(CV);
        g.addVertex(CV);
    }

    public void addVertex(CityVertex vertex){
        vertices.add(vertex);
        g.addVertex(vertex);
    }

    public void addVertex(String sVertex){
        this.addVertex(createVertex(sVertex));
    }

    public void addEdge(String sVertex1,String sVertex2){
        CityVertex vertex1 = this.getVertex(sVertex1);
        CityVertex vertex2 = this.getVertex(sVertex2);
        this.addEdge(vertex1,vertex2);
    }

    public void addEdge(CityVertex vertex1,CityVertex vertex2){
        this.g.addEdge(vertex1,vertex2,vertex1.vertexDist(vertex2));
    }

    public void addEdge(String edge){
        String[] sVertices = edge.split(":");
        addEdge(sVertices[0],sVertices[1]);
    }

    public  CityVertex createVertex(String coordinates){
        String[] xy = coordinates.split(",");
        return new CityVertex(coordinates,Float.parseFloat(xy[0]),Float.parseFloat(xy[1]));
    }



    public CityVertex getVertex(String vertex){

        for (CityVertex cityVertex : vertices) {
            if (cityVertex.equals(vertex)) {
                return cityVertex;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        BrabaLog bl = new BrabaLog();
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine());

        for(int i = 0;i<n;i++) {
            String coordinates = scanner.nextLine();
            bl.addVertex(coordinates);
        }
        n = Integer.parseInt(scanner.nextLine());
        for(int i = 0;i<n;i++) {
            String edge = scanner.nextLine();
            bl.addEdge(edge);
        }
        bl.setTraversal();
        bl.traverse();
        //bl.printDistanceMatrix();
        System.out.println(bl.getCenterMost().getName());
        Vertex peripheral = bl.getMostPeripheral();
        Vertex furthest = bl.getFurthest(peripheral);
        System.out.println(peripheral.getName());
        System.out.println(furthest.getName());
    }

    public void setTraversal(){
        FWTraversal = new FloydWarshallTraversal(g);
    }

    public void traverse(){
        FWTraversal.traverseGraph(g.getVertices().get(0));
    }

    public void printDistanceMatrix(){
        FWTraversal.printDistanceMatrix();
    }
    public Vertex getCenterMost(){
        return g.getCenterMostVertex(FWTraversal.getDistanceMatrix());
    }

    public Vertex getMostPeripheral(){
        return g.getMostPeripheralVertex(FWTraversal.getDistanceMatrix());
    }
    public Vertex getFurthest(Vertex source){
        int index = g.getVertices().indexOf(source);
        float[][] distMatrix= FWTraversal.getDistanceMatrix();
        int furthest = 0;
        float dist = Float.NEGATIVE_INFINITY;
        for (int j=0; j<distMatrix[index].length; j++){
            if (distMatrix[index][j]>dist){
                dist = distMatrix[index][j];
                furthest = j;
            }
        }
        return g.getVertices().get(furthest);
    }


}
