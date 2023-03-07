package me.loop.client.modules;

public enum Category {
    COMBAT("Combat"),
    MISC("Misc"),
    RENDER("Render"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    CLIENT("Client"),
    TEST("Test");

    public String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
