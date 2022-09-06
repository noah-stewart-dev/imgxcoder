public class ColorComponentEnum {
    public enum ColorComponent {
        ALPHA(6),
        RED(4),
        GREEN(2),
        BLUE(0);

        private int componentBitPosition;

        private ColorComponent(int startingBitPosition) {
            this.componentBitPosition = componentBitPosition;
        }

        public int getComponentBitPosition() {
            return componentBitPosition;
        }
    }
}