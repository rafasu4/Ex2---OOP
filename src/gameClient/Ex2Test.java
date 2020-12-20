package gameClient;


import api.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Ex2Test {



    @Test
    void toWalk() {
    }

    @Test
    void pokemonOnEdge() {
    }

    @Test
    void deserializer() {
        DWGraph_Algo k = new DWGraph_Algo();
        k.load("C:/Users/Stycks/IDEA/Ex2/data/A0.json");
        directed_weighted_graph g = k.getGraph();
        assertEquals(g,k.getGraph());
    }

    @Test
    void startMenu() {
    }

    @Test
    void actionPerformed() {
    }
}