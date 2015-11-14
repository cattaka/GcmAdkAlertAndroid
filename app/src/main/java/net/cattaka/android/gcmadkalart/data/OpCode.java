
package net.cattaka.android.gcmadkalart.data;

public enum OpCode {
    LED_RGB((byte)1), //
    UNKNOWN((byte)-1);

    private byte value;

    OpCode(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static OpCode fromValue(byte value) {
        for (OpCode oc : values()) {
            if (oc.getValue() == value) {
                return oc;
            }
        }
        return UNKNOWN;
    }

}
