package concurrency

/*
 * Written by: Jose Carlos Carrasco Jimenez
 * Purpose: The purpose of this piece of software is to show the use of Actors in Scala. It is a very simple 
 * 			program that makes use of some of the simple features of Actors in Scala. The example represents 
 *    		a (unofficial) dialogue between Romeo and Juliet taken from: "http://whimquarterly.com/conversation-pieces
 *      	/a-conversation-between-romeo-and-juliet-two-shakespearian-characters-i-know-very-little-about"
 */

import scala.actors.Actor
import scala.actors.Actor._
import scala.collection.immutable.Stack

object RomeoJuliet {

  def main(args: Array[String]) {
    var romeoScript: List[String] = List("IÕm not. I remember either thinking that it was this way and that it was weird Shakespeare named us this, or that is was not that way because Shakespeare would never have named you that.",
        "They are like the Bloods and the Crips.",
        "And Friar Tuck is someone.",
        "A friar is like a monk?",
        "And another thing I know about us is that even though it seems like we are adults, we are actually teenagers. Or maybe like 20 or something, but I think even younger.",
        "Holy shit, I just realized Friar Tuck has nothing to do with us.",
        "I defintiely know that name. UmmÉ.",
        "Robin Hood! Exactly. And that was written a long time after we died?",
        "Or 1400s, but yes, long before Robin Hood.",
        "Yes, you pretended to kill yourself and then I thought you really did and then I killed myself and then you saw that I did and then you did.",
        "Yes, but definitely not Friar Tuck.")
    var julietScript: List[String] = List("Juliet Capulet? Sounds terrible. Are you sure?",
        "Either way, I know one thing for sure: Our families the Montagues and the Capulets hate each other.",
        "They are exactly like the Bloods and the Crips.",
        "He is definitely someone. And a friar is a religious person, like a priest, but maybe a step below that.",
        "Sure.",
        "Yes, you are mostly raping me throughout this classic love story.",
        "What is Friar Tuck from?",
        "Robin Hood!",
        "Yes, probably hundreds of years. We were alive in the 1600s.",
        "And just to review, the way we both died was suicide.",
        "WasnÕt there a friar involved, though?",
        "Little John is someone from Robin Hood, too.")
        
    val juliet = new Juliet(julietScript)
    val romeo = new Romeo(romeoScript, juliet)

    romeo.start
    juliet.start
  }

  case object Act
  case object Stop

  class Romeo(script: List[String], otherActor: Actor) extends Actor {
    def act() {
      var line = 0
      otherActor ! Act

      while (true) {
        receive {
          case Act =>
            if (line < script.length) { println("Romeo: " + script(line)); Thread.sleep(5000); line += 1; otherActor ! Act } //print part of script
            else { println("ROMEO: THE END!"); otherActor ! Stop; exit() }
        }
      }
    }
  }

  class Juliet(script: List[String]) extends Actor {
    def act() {
      var line = 0
      while (true) {
        receive {
          case Act =>
            if (line < script.length) { println("Juliet: " + script(line)); Thread.sleep(5000); line +=1; sender ! Act }
            else {println("JULIET: THE END!"); sender ! Act}
          case Stop =>
            exit()
        }
      }
    }
  }

}
