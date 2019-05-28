package streams

import common._

/**
 * This component implements the solver for the Bloxorz game
 */
trait Solver extends GameDef {

  /**
   * Returns `true` if the block `b` is at the final position
   */
  def done(b: Block): Boolean = (b == Block(goal,goal)) && b.isStanding && b.isLegal


  /**
   * This function takes two arguments: the current block `b` and
   * a list of moves `history` that was required to reach the
   * position of `b`.
   *
   * The `head` element of the `history` list is the latest move
   * that was executed, i.e. the last move that was performed for
   * the block to end up at position `b`.
   *
   * The function returns a stream of pairs: the first element of
   * the each pair is a neighboring block, and the second element
   * is the augmented history of moves required to reach this block.
   *
   * It should only return valid neighbors, i.e. block positions
   * that are inside the terrain.
   */
  def neighborsWithHistory(b: Block, history: List[Move]): Stream[(Block, List[Move])] = {
    /*
    if (!b.isLegal) Stream() else
    (for {
      i <- b.legalNeighbors   // return all legal neighbours
      j <- i._1.legalNeighbors  // return all legal neighbours of this neighbour
      // check if the legal neighbour is the same as block and the moves is the same as the history.head
      if (Pos(j._1.b1.row,j._1.b1.col) == Pos(b.b1.row,b.b1.col))&&(Pos(j._1.b2.row,j._1.b2.col) == Pos(b.b2.row,b.b2.col))
      if j._2 == history.head
    } yield (j._1, history.tail)).toStream  */  // This function supposes to retrieve backwards. but the purpose required by the instruction is not

    // The instruction just require to return all neighbour of b and the history (including history to reach b) to reach those blocks

    (for {
      i <- b.legalNeighbors
    } yield (i._1, i._2::history)).toStream

  }

  /**
   * This function returns the list of neighbors without the block
   * positions that have already been explored. We will use it to
   * make sure that we don't explore circular paths.
   */
  def newNeighborsOnly(neighbors: Stream[(Block, List[Move])],
                       explored: Set[Block]): Stream[(Block, List[Move])] = {
    neighbors filterNot(x => explored contains x._1)
  }

  /**
   * The function `from` returns the stream of all possible paths
   * that can be followed, starting at the `head` of the `initial`
   * stream.
   *
   * The blocks in the stream `initial` are sorted by ascending path
   * length: the block positions with the shortest paths (length of
   * move list) are at the head of the stream.
   *
   * The parameter `explored` is a set of block positions that have
   * been visited before, on the path to any of the blocks in the
   * stream `initial`. When search reaches a block that has already
   * been explored before, that position should not be included a
   * second time to avoid cycles.
   *
   * The resulting stream should be sorted by ascending path length,
   * i.e. the block positions that can be reached with the fewest
   * amount of moves should appear first in the stream.
   *
   * Note: the solution should not look at or compare the lengths
   * of different paths - the implementation should naturally
   * construct the correctly sorted stream.
   */
  def from(initial: Stream[(Block, List[Move])],
           explored: Set[Block]): Stream[(Block, List[Move])] = initial match {
    case Stream.Empty => Stream.Empty
    case (block, mvlist) #:: tail => {
      val ExploredAcc = explored + block
      val NeighborsAcc = newNeighborsOnly(neighborsWithHistory(block, mvlist), ExploredAcc)

      NeighborsAcc ++ from(tail ++ NeighborsAcc, ExploredAcc)
    }
  }


  /**
   * The stream of all paths that begin at the starting block.
   */
  lazy val pathsFromStart: Stream[(Block, List[Move])] = from((startBlock, List[Move]()) #:: Stream[(Block, List[Move])](), Set())

  /**
   * Returns a stream of all possible pairs of the goal block along
   * with the history how it was reached.
   */
  lazy val pathsToGoal: Stream[(Block, List[Move])] = {
    pathsFromStart filter {
      case (block, _) => done(block)
    }
  }

  /**
   * The (or one of the) shortest sequence(s) of moves to reach the
   * goal. If the goal cannot be reached, the empty list is returned.
   *
   * Note: the `head` element of the returned list should represent
   * the first move that the player should perform from the starting
   * position.
   */
  lazy val solution: List[Move] = pathsToGoal match {
    case Stream.Empty => List()
    case (_, mvlist) #:: tail => mvlist.reverse
  }
}