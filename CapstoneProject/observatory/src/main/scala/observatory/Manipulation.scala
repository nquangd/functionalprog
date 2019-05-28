package observatory

/**
  * 4th milestone: value-added information
  */
object Manipulation {
//// par.map in prediction would cause overhead parallel in makeGrid. This is the cause of TimeOut
  /**
    * @param temperatures Known temperatures
    * @return A function that, given a latitude in [-89, 90] and a longitude in [-180, 179],
    *         returns the predicted temperature at this location
    */

  def lazygrid(temperatures: Iterable[(Location, Temperature)]): temparray = {
    val grid = new temparray
    grid.calculate(temperatures)
    grid
  }

  def makeGrid(temperatures: Iterable[(Location, Temperature)]): GridLocation => Temperature = {
    lazy val grid : Map[GridLocation,Temperature] = (for {
      lat <- -89 to 90 by 1
      lon <- -180 to 179 by 1
    } yield {
      GridLocation(lat,lon) -> Visualization.predictTemperature(temperatures, Location(lat.toDouble, lon.toDouble))
    }
    ).toMap
    loc:GridLocation => grid(loc)



            /*
          lazy val init = for {
            lat <- -89 to 90 by 1
            lon <- -180 to 179 by 1
          } yield GridLocation(lat,lon)

          val grid = init.par.map(loc => GridLocation(loc.lat,loc.lon) ->
            Visualization.predictTemperature(temperatures,Location(loc.lat.toDouble,loc.lon.toDouble)))
    loc:GridLocation => grid(loc)

      */
          /*
       val grid = lazygrid(temperatures)
     loc:GridLocation => grid.retrive(loc.lat,loc.lon)
*/
  }

  /**
    * @param temperaturess Sequence of known temperatures over the years (each element of the collection
    *                      is a collection of pairs of location and temperature)
    * @return A function that, given a latitude and a longitude, returns the average temperature at this location
    */
  def average(temperaturess: Iterable[Iterable[(Location, Temperature)]]): GridLocation => Temperature = {

/*
    val alltemp = temperaturess.par.map(lazygrid)
        .reduce((a:temparray,b:temparray) => a += b)

    alltemp /= temperaturess.size

    loc:GridLocation => alltemp.retrive(loc.lat,loc.lon)

    */


     lazy val alltemp: Iterable[GridLocation => Temperature ] = temperaturess.par.map(makeGrid).seq
    loc:GridLocation => {
      val temp = alltemp.map(x => x(loc))
      temp.sum/temp.size
    }


  }

  /**
    * @param temperatures Known temperatures
    * @param normals A grid containing the “normal” temperatures
    * @return A grid containing the deviations compared to the normal temperatures
    */
  def deviation(temperatures: Iterable[(Location, Temperature)], normals: GridLocation => Temperature): GridLocation => Temperature = {
    lazy val thistemp = makeGrid(temperatures)
    loc:GridLocation => thistemp(loc) - normals(loc)


            /*
    val thistemp = lazygrid(temperatures)

    thistemp -= normals
    loc:GridLocation => thistemp.retrive(loc.lat,loc.lon)

    */
  }


}

