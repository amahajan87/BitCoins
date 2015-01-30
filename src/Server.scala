import akka.actor.Props
import scala.concurrent.duration._
import akka.actor.ReceiveTimeout
import akka.cluster.Cluster
import akka.cluster.ClusterEvent
import akka.actor.RootActorPath
import akka.actor.Identify
import akka.actor.ActorIdentity
import akka.actor.ActorLogging
import akka.actor.Terminated
import akka.actor._


class Server(noOfZeros : Int) extends Actor {
  
//val start : Long = System.currentTimeMillis
val serverNode = Cluster(context.system)
  serverNode.subscribe(self, classOf[ClusterEvent.MemberUp])
  serverNode.subscribe(self, classOf[ClusterEvent.MemberRemoved])
  serverNode.join(serverNode.selfAddress) 
  
val boss = context.actorOf(Props[Boss], name = "boss")
println(boss + "                              " + boss.path)
boss ! ServerGeneratedBitcoins(noOfZeros)
//println(self.path)
context.watch(boss)

//var count = -1



 def receive = {
    case ClusterEvent.MemberUp(member) => 
  
    if(member.address != serverNode.selfAddress)
    { println("*****************Member joined******************************")
      
     boss ! GenerateBitcoins(noOfZeros,0)
    }
     case TotalBitcoins(totalCoins) => println("reached server printing position" )
      totalCoins.foreach(println)
      context.stop(self)
     case ClusterEvent.MemberRemoved(m, _) =>
      context.stop(self)
  }







}

