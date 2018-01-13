package cs220.actors

import akka.actor.Actor
import akka.actor.{ActorRef, ActorLogging}

/**
 * The `ParseActor` handles the parsing of HTML documents. Its
 * primary goal is to extract out the links and words contained
 * in an HTML document.
 */
class ParseActor(pq: ActorRef) extends Actor with ActorLogging {
  log.info("ParseActor created")
  pq ! NeedPage

  def receive = {
    // TODO
    case ParsePage(url, html) => {
	var hurls = html
	val ur1 = """(?s)<head.*>.*</head>""".r
    	ur1.replaceAllIn(hurls, "")
	val ur2 = """(?s)<script.*>.*</script>""".r
    	ur2.replaceAllIn(hurls, "")
	val ur3 = """(?s)<style.*>.*</style>""".r
    	ur3.replaceAllIn(hurls, "")
	val ur4 = """(?s)<!--.*-->""".r
    	ur4.replaceAllIn(hurls, "")
	val re1 = """(https?://[^\"]+)""".r
	val m = re1.findAllIn(hurls).toList
	hwords = hurls
	val wr1 = """</?[^>]+>""".r
    	wr1.replaceAllIn(hwords, "")
	val wr2 = """[\n\r]""".r
    	wr2.replaceAllIn(hwords, " ")
	val re2 = """</?[^>]+>""".r
	val n = re2.findAllIn(hwords).toList
	for(a <- m){
		pq ! Link(a)
	}
	for(b <- n){
		pq ! Word(url, b)
	}
	pq ! NeedPage
    }

    case NoPages => {
	pq ! Needpage
    }
  }
}
