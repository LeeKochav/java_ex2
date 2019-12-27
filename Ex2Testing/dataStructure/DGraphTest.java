package dataStructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class DGraphTest {
    Node n0;
    Node n1;
    Node n2;
    Node n3;
    Node n4;
    Node n5;
    DGraph g;

    @BeforeEach
    void init() {
        n0 = new Node();
        n1 = new Node();
        n2 = new Node();
        n3 = new Node();
        n4 = new Node();
        n5 = new Node();
        g = new DGraph();
        g.addNode(n0);
        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);
        g.addNode(n4);
        g.addNode(n5);
        g.connect(0, 5, 10);
        g.connect(0, 2, 20);
        g.connect(5, 1, 25);
        g.connect(5, 3, 7);
        g.connect(2, 3, 30);
        g.connect(1, 4, 4);
        g.connect(3, 4, 2);

    }

    @Test
    void getNode() {
        assertTrue(g.getNode(6) == null);
        assertEquals(g.getNode(5).getKey(), n5.getKey());
    }

    @Test
    void getEdge() {
        assertTrue(g.getEdge(1, 6) == null);
        assertEquals(g.getEdge(3, 4).getWeight(), 2.0);
    }

    @Test
    void addNode() {
        Node n6 = new Node();
        g.addNode(n6);
        assertEquals(g.getNode(6).getKey(), 6);
        try {
            g.addNode(null);
            fail("addNode function did not throw exception when node is null");
        } catch (RuntimeException e) {
            System.out.println("addNode did throw exception when node is null : " + e.getMessage());
        }
    }

    @Test
    void connect() {
        Node n6 = new Node();
        g.addNode(n6);
        g.connect(1, 6, 1);
        g.connect(4, 6, 3);
        assertEquals(g.getEdge(1, 6).getWeight(), 1.0);
        assertEquals(g.getEdge(4, 6).getWeight(), 3.0);
        try {
            g.connect(10, 20, 5);
            fail("Connect function did not throw exception when input is not valid");
        } catch (RuntimeException error) {
            System.out.println("Connect function throw exception when input is not valid: " + error.getMessage());
        }
    }

    @Test
    void getV() {
        DGraph g2 = new DGraph();
        assertTrue(g.getV().size() == 6);
        assertTrue(g2.getV().size() == 0);
    }

    @Test
    void getE() {
        assertTrue(g.getE(0).size() == 2);
    }

    @Test
    void removeNode() {
        assertTrue(g.removeNode(6) == null);
        int tmpEdgeSize = g.edgeSize();
        int tmpNodeSize = g.nodeSize();
        g.removeNode(5);
        assertTrue(g.nodeSize() == tmpNodeSize - 1);
        assertTrue(g.edgeSize() == tmpEdgeSize - 3);

    }

    @Test
    void removeEdge() {
        assertTrue(g.removeEdge(1, 6) == null);
        int tmpEdgeSize = g.edgeSize();
        g.removeEdge(0, 5);
        assertEquals(g.edgeSize(), tmpEdgeSize - 1);

    }

    @Test
    void nodeSize() {
        int s = g.nodeSize();
        assertEquals(g.nodeSize(), s);
    }

    @Test
    void edgeSize() {
        int s = g.edgeSize();
        assertEquals(g.edgeSize(), s);
    }

    @Test
    void performance() {
        assertTimeout(Duration.ofMillis(100000), () -> {
            int million = 1000000;
            DGraph d = new DGraph(million);
            for (node_data n : d.getV()) {
                for (int i = 1; i <= 10; i++) {
                    try {
                        d.connect(n.getKey(), n.getKey() + i, i * 5);
                    } catch (RuntimeException e) {
                    }
                }
            }
        });
    }
}