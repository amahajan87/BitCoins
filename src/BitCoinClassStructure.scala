

import akka.actor._
import akka.routing.RoundRobinRouter
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import java.security._
import java.security.MessageDigest
import collection.mutable.ListBuffer
import akka.cluster.Cluster

  
sealed trait BitMsg
case class GenerateBitcoins(noOfZeros : Int, count: Int) extends BitMsg
case class InitiateGeneration(gatorId : String, first : Int, last : Int, noOfZeros : Int) extends BitMsg
case class BitcoinsGenerated(Keys : ListBuffer[Tuple2[String, String]]) extends BitMsg
case class TotalBitcoins(totalCoins : ListBuffer[Tuple2[String, String]])
case class ServerGeneratedBitcoins(noOFZeros : Int) extends BitMsg


class Worker extends Actor {
  
  def receive = {
    
    case InitiateGeneration(gatorId : String, first : Int, last : Int, noOfZeros : Int) =>
      sender ! BitcoinsGenerated(findBitcoins(gatorId,first,last,noOfZeros))
      
           
         
   }
  
  
  private def findBitcoins(gatorId : String, first : Int, last : Int, noOfZeros : Int) : ListBuffer[Tuple2[String,String]] = {
    
    val bits = ListBuffer[Tuple2[String,String]]()
   
    var zeroString = "0" 
    
      for ( i <- 0 to noOfZeros - 2 ) 
      zeroString += "0"  
    
        
        println("no of zeros = " + noOfZeros)
 
    
    for (i <- first to last)
    {   val s = gatorId + i.toString
	   val hash : String = MessageDigest.getInstance("SHA-256").digest(s.getBytes)
			   .foldLeft("")((s: String, b: Byte) => s +
			   Character.forDigit((b & 0xf0) >> 4, 16) +
			   Character.forDigit(b & 0x0f, 16))
			   
			   if(hash.startsWith(zeroString)) bits += ((s , hash))
			     
    }
   // bits.foreach(println)
    bits
  
  }

 }
