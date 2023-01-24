/**
 * This class represents a group of tiles. It has a value to add
 * towards the total sum of the game to work towards the goal. 
 *
 */
public class Group {
	private Tile tile1;
	private Tile tile2;
	
	/**
	 * Constructor with only one tile. This will create
	 * a group with a single tile.
	 * @param tile The single tile to add to the group.
	 */
	public Group(Tile tile) {
		this.tile1 = tile;
		this.tile2 = null;
	}
	
	/**
	 * Constructor with two tiles. This will create
	 * a group with two tiles.
	 * @param tile1 The first tile to add to the group.
	 * @param tile2 The second tile to add to the group.
	 */
	public Group(Tile tile1, Tile tile2) {
		this.tile1 = tile1;
		this.tile2 = tile2;
	}
	
	/**
	 * Method to remove a tile from a group.
	 * @param tileToRemove The tile to remove from the group
	 */
	public void removeTile(Tile tileToRemove) {
		if(tileToRemove != null) {
			if(compareTiles(this.tile1, tileToRemove))
				this.tile1 = null;
			else if(compareTiles(this.tile2, tileToRemove))
				this.tile2 = null;
		}
	}
	
	/**
	 * Compares two tiles with their position on the tile panel and
	 * checks if they are the same.
	 * @param tile1 The first tile to compare.
	 * @param tile2 The second tile to compare.
	 * @return True if they are equal or false if they are not.
	 */
	private boolean compareTiles(Tile tile1, Tile tile2) {
		boolean isEqual = false;
		if (tile1 != null && tile2 != null) {
			if(tile1.getPosition() == tile2.getPosition())
				isEqual = true;
		}
		
		return isEqual;
	}
	
	/**
	 * Checks if a tile is contained in this group.
	 * @param tileToFind The tile to find in the group.
	 * @return True if the tile is in this group or false if it is not.
	 */
	public boolean contains(Tile tileToFind) {
		boolean tileFound = false;
		
		if(tile1 != null) {
			if(tile1.getPosition() == tileToFind.getPosition())
				tileFound = true;
		}
		if(tile2 != null) {
			if(tile2.getPosition() == tileToFind.getPosition())
				tileFound = true;
		}
		return tileFound;
	}
	
	/**
	 * Checks if a group is empty. A group is empty when both its tiles are null.
	 * @return True if the group is empty or false if it is not.
	 */
	public boolean isEmpty() {
		boolean isEmpty = false;
		if(this.tile1 == null && this.tile2 == null)
			isEmpty = true;	
		return isEmpty;
	}
	
	/**
	 * Returns the sum of the values for this group.
	 * @return The total sum of this group.
	 */
	public int getSum() {
		int sum = 0;
		// Quick exit if group is empty
		if(this.isEmpty())
			return 0;
		if(this.tile1 == null)
			sum = this.tile2.getValue();
		else if(this.tile2 == null)
			sum = this.tile1.getValue();
		else
			sum =  Integer.parseInt(Integer.toString(tile1.getValue()) + Integer.toString(tile2.getValue()));
		
		return sum;
	}
}
