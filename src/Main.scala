import akka.actor._



object Main {

  def main(args : Array[String]) = {
    
    val system = ActorSystem("project1")
    if (args(0).length<2) 
    { 
      val noOfZeros = Integer.parseInt(args(0))
      
      val server = system.actorOf(Props(new Server(noOfZeros)), "BitCoinsServer")
    }
      else 
      { println(args(0))
        val client = system.actorOf(Props(new RemoteNode(args(0))), "BitCoinsClient")
      }
    
  }
 
}