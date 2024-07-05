package org.example.server.model;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

public class FieldTest {

    @Test
    public void testFieldGenerateWithCorrectSize() throws Exception{
        for(int i=2; i<=50; i++) {
            Field field = new Field(i);
            assertEquals(i, field.getSize());
            assertEquals(i*i, field.getResult().size());
        }
    }

    @Test
    public void testFieldGenerateWithUncorrectSize() {
        for (int i = -10; i < 2; i++) {
            final int size = i;
            assertThrows(IOException.class, () -> {new Field(size);});
        }

        for (int i = 51; i < 61; i++) {
            final int size = i;
            assertThrows(IOException.class, () -> {new Field(size);});
        }
    }

    @Test
    public void testCopyField() throws Exception {
        Field field = new Field(25);
        Field copy = field.copy();
        assertEquals(field, copy);
    }
}
