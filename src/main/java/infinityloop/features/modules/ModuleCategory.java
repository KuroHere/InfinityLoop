package infinityloop.features.modules;

public enum ModuleCategory {
    COMBAT("Combat"),
    MISC("Misc"),
    RENDER("Render"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    CLIENT("Client"),
    TEST("Test");

    public String name;

    ModuleCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
