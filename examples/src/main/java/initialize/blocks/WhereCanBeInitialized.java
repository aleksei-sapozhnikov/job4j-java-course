package initialize.blocks;

public class WhereCanBeInitialized {

    // static
    private final static int STATIC_IN_DECLARATION = 4;
    private final static int STATIC_IN_STATIC_BLOCK;
    private static int staticNonFinalInDynamicBlock;

    // static block
    static {
        STATIC_IN_STATIC_BLOCK = 6;
    }

    //non-static
    private final int dynamicInDeclaration = 3;
    private final int dynamicInDynamicBlock;
    private final int dynamicInConstructor;

    // dynamic block
    {
        staticNonFinalInDynamicBlock = 5;
        dynamicInDynamicBlock = 23;
    }

    //constructor
    WhereCanBeInitialized() {
        dynamicInConstructor = 23;
    }

}
