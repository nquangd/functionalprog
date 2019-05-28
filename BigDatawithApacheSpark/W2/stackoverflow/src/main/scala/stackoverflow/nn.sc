val l1 = List((5000,1),(5000,2),(4000,1),(4000,2),(4000,3))

l1.map(_._2).toList


l1.maxBy(_._2)._2

l1.groupBy(a => a._1).mapValues(_.length).toList.sortBy(_._2).reverse