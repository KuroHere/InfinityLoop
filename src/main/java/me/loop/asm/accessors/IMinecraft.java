package me.loop.asm.accessors;

public interface IMinecraft {

    /**
     * @return the current gameloop, will be incremented every gameloop.
     */
    int getGameLoop();
}
