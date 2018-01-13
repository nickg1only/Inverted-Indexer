package cs220.actors

import scala.collection.mutable.Queue
import akka.actor.Actor
import akka.actor.{ActorRef, ActorLogging}

/**
 * The `ParseQueueActor` is responsible for maintaining a queue of pages
 * to be parsed by the `ParseActor`s. It is responsible for determining
 * if a page has already been parsed. If so, it should not parse it again.
 */
class ParseQueueActor(indexer: ActorRef) extends Actor with ActorLogging {
  var linkQueue: Option[ActorRef] = None
  val queue = Queue[ParsePage]()

  def receive = {
    case Page(url, html) => {
      if (linkQueue == None)
        linkQueue = Some(sender)
      // TODO
      indexer!CheckPage(url, html)
    }
    // MORE TO BE DONE HERE...
    case ParsePage(url, html) => {
      queue = queue :+ ParsePage(url, html)
    }

    case NeedPage => {
      if(!queue.isEmpty){
         indexer!queue.dequeue()
      }
      else{
        indexer!NoPages
      }
    }

    case Link(url) => {
      indexer!CheckLink
    }

    case Word(url, word) => {
      indexer!Word(url, word)
    }

    case QueueLink(url) => {
      indexer!QueueLink(url)
    }
  } 
}
