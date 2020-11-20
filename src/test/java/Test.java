import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Test {

	public static void main(String[] args) {
	}

	public static class MyComponent extends Component {

	}

	public static class GridPanel<C extends Component> extends JPanel implements Iterable<C> {

		private final int rows, columns;
		private final C[] components;

		@SuppressWarnings("unchecked")
		public GridPanel(int xSize, int ySize, Supplier<C> componentSupplier) {
			this.rows = xSize;
			this.columns = ySize;
			this.components = (C[])new Component[this.rows * this.columns];

			this.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();

			for (int x = 0; x < xSize; x++) {
				gbc.gridx = x;
				for (int y = 0; y < ySize; y++) {
					gbc.gridy = y;
					this.components[this.getIndex(x, y)] = componentSupplier.get();
					this.add(this.components[this.getIndex(x, y)], gbc);
				}
			}
		}

		private int getIndex(int row, int column) {
			return column * this.rows + row;
		}

		public C getComponent(int row, int column) {
			return this.components[this.getIndex(row, column)];
		}

		public int getRows() {
			return this.rows;
		}

		public int getColumns() {
			return this.columns;
		}

		@Override
		public C[] getComponents() {
			return this.components;
		}

		@Override
		public void forEach(Consumer<? super C> action) {
			Arrays.stream(this.components).forEach(action);
		}

		@Override
		public Spliterator<C> spliterator() {
			return Arrays.stream(this.components).spliterator();
		}

		@Override
		public Iterator<C> iterator() {
			return Arrays.stream(this.components).iterator();
		}
	}

}
