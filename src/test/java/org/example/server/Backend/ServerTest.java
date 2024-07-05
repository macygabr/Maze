package org.example.server.Backend;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.example.server.database.Database;
import org.example.server.model.Cell;
import org.example.server.model.Field;
import org.example.server.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class ServerTest {
    @Mock
    private Database database;

    @Test
    public void testCreateServer() throws Exception {
        Field field = new Field();
        Server server = new Server(field, database);
        assertEquals(field, server.getField());
    }

    @Test
    public void testFindPath() throws Exception {
        String cookie = "tester";
        Field field = new Field();
        User user = User.builder().field(field).cookie(cookie).build();
        Server server = new Server(field, database);
        server.getUsers().put(cookie, user);

        Server serverWtitPath = server.FindPath(cookie);

        int count = 0;
        for(Cell cell : serverWtitPath.getField().getResult()) {
            if(cell.getPath()) count++;
        }
        assertNotEquals(count, 0);
    }
}
