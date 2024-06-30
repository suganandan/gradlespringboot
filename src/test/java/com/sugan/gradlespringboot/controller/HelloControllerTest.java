package com.sugan.gradlespringboot.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HelloControllerTest {
    @Test
    public void testAddNumbers() {
        HelloController controller = new HelloController();
        int result = controller.addNumbers(2, 3);
        Assertions.assertEquals(5, result);

        result = controller.addNumbers(-1, 1);
        Assertions.assertEquals(0, result);

        result = controller.addNumbers(0, 0);
        Assertions.assertEquals(0, result);
    }
}
