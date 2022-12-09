package com.dbjgb.advent.twentyTwo.puzzle._09;

public class RopeEnd {

  private Cell position;
  private final RopeEnd rear;
  private final int id;

  public RopeEnd(int id, Cell position) {
    this(id, null, position);
  }

  public RopeEnd(int id, RopeEnd rear, Cell position) {
    this.id = id;
    this.rear = rear;
    this.position = position;
    ensureEndIsAdjacent();
  }

  public Cell getPosition() {
    return position;
  }

  public void setPosition(Cell position) {
    this.position = position;
  }

  public void move(Direction direction) {
    this.position = direction.apply(position);
    ensureEndIsAdjacent();
  }

  private void ensureEndIsAdjacent() {
    if (rear != null && !position.adjacentTo(rear.getPosition())) {
      Direction rowDirection =
          Direction.valueByDeltaRow(position.getRowDifferenceComparedTo(rear.getPosition()));
      Direction columnDirection =
          Direction.valueByDeltaColumn(position.getColumnDifferenceComparedTo(rear.getPosition()));
      rear.setPosition(rowDirection.apply(columnDirection.apply(rear.getPosition())));
      rear.ensureEndIsAdjacent();
    }
  }
}
