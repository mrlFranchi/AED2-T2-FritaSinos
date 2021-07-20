package graph;

/*
    Murilo Franchi nrUSP 9790760
    classe herdada de vertice, para guardar as coordenadas
 */
public class CityVertex extends Vertex{
    private double x;
    private double y;

    public CityVertex(String name, double x, double y) {
        super(name);
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public float vertexDist(CityVertex otherVertex){
        double otherX,otherY;
        otherX = otherVertex.getX();
        otherY = otherVertex.getY();
        return (float)Math.sqrt((x-otherX)*(x-otherX)+(y-otherY)*(y-otherY));
    }
    public boolean equals(CityVertex otherVertex){
        return otherVertex.getName().equals(this.getName());
    }
    public boolean equals(String otherVertex){
        return this.getName().equals(otherVertex);
    }

}
