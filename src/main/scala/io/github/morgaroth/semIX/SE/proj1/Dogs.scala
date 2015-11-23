package io.github.morgaroth.semIX.SE.proj1

import java.io.File
import java.nio.file.Files

import org.jpl7._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future, Promise}
import scala.io.Source
import scala.language.postfixOps
import scala.swing._
import scala.swing.event.ButtonClicked
import scala.util.{Failure, Success}

object Dogs extends SimpleSwingApplication {

  JPL.init()

  def top = new MainFrame {
    var ac: Promise[Boolean] = null
    var gameStarted = false
    var gameEnded = false
    val propositions = scala.collection.mutable.MutableList.empty[String]

    val questionLabel = new Label("HERE IS QUESTION")
    val questionHeader = new Label("Odpowiedz na pytanie:")
    val yesButton = new Button("TAK")
    val noButton = new Button("NIE")
    val restart = new Button("START")
    val propositionText = new Label("HERE IS PROPOSTION")
    propositionText.preferredSize = new Dimension(250, 20)
    val loadBtn = new Button("Load prolog file and start!")
    val code = new TextArea()
    val propositionHeader: Label = new Label("Propozycja dla Ciebie to:")
    propositionHeader.visible = false
    propositionText.visible = false

    val listener = new Questions {
      override def ask(question: String): Boolean = {
        println(s"listener called with question $question")
        questionLabel.text = question
        //        proposition.text = possibilities.mkString(", ")
        ac = Promise[Boolean]
        Await.result(ac.future, 1 hour)
      }

      override def result(p: String) = {
        if (p != "nic") propositions += p
        propositionHeader.visible = true
        propositionText.visible = true
        if (propositions.isEmpty) propositionText.text = "Brak rozwiazan - kup sobie rybki"
        else propositionText.text = propositions.mkString(", ")
      }

    }
    BRIDGE.setListener(listener)
    title = "Dog"
    //    preferredSize = new Dimension(300, 300)
    resizable = true
    contents = new GridPanel(6, 1) {
      contents += loadBtn
      contents += questionHeader
      contents += questionLabel
      contents += new GridPanel(1, 2) {
        contents += yesButton
        contents += noButton
      }
      contents += propositionHeader
      contents += propositionText
      //          contents += restart
    }
    listenTo(yesButton, noButton, loadBtn)

    reactions += {
      case ButtonClicked(`loadBtn`) if !gameStarted =>
        println("load file clicked")
        loadAndStart()
        println("loaded")
        loadBtn.visible = false
        gameStarted = true
      case ButtonClicked(`loadBtn`) =>
      case ButtonClicked(`restart`) =>
        println("start clicked")
        restart.text = "Restart"
        reloadGame(code)
      case ButtonClicked(`noButton`) if gameStarted && !gameEnded =>
        println("no clicked")
        ac.success(false)
      case ButtonClicked(`yesButton`) if gameStarted && !gameEnded =>
        println("yes clicked")
        ac.success(true)
    }

    def startGame(): Unit = {
      val q = new Query("start")
      q.open()
      println("opened")
      println(s"hasSolution ${q.hasNext}")
      println("closing")
      q.close()
      gameEnded = true
      propositionHeader.visible = true
      propositionText.visible = true
      if (propositions.isEmpty) propositionText.text = "Brak rozwiazan - kup sobie rybki"
      else propositionText.text = propositions.mkString(", ")
      yesButton.visible = false
      noButton.visible = false
      questionHeader.visible = false
      questionLabel.visible = false
    }

    def reloadGame(code: TextArea): Unit = {
      println(s"reloading game")
      JPL.init()
      val f = Files.createTempFile("a", ".pl").toFile
      printToFile(f) {
        _.println(code.text)
      }
      loadPrologFile(f.getCanonicalPath)
      startGame()
    }

    def loadAndStart(): Unit = {
      val f = Future {
        println(s"lazy reloading game")
        val jc = new FileChooser(new File("src/main/resources"))
        val r = jc.showOpenDialog(null)
        if (r == FileChooser.Result.Approve) {
          println(s"selected ${jc.selectedFile.getAbsolutePath}")
          loadPrologFile(jc.selectedFile.getCanonicalPath)
          startGame()
        }
      }
      f.onComplete {
        case Success(value) => println(s"start game end with $value")
        case Failure(t) =>
          t.printStackTrace()
      }
    }

    def loadProlog(code: TextArea) = {
      val jc = new FileChooser(new File("src/main/resources"))
      val r = jc.showOpenDialog(null)
      if (r == FileChooser.Result.Approve) {
        println(s"selected ${jc.selectedFile.getAbsolutePath}")
        code.text = Source.fromFile(jc.selectedFile).mkString
      }
    }

    def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
      val p = new java.io.PrintWriter(f)
      try {
        op(p)
      } finally {
        p.close()
      }
    }

    def loadPrologFile(filePath: String): Unit = {
      JPL.init()
      println(s"loading prolog file $filePath")
      val consultQuery = new Query("consult", Array[Term](new Atom(filePath)))
      if (!consultQuery.hasSolution) {
        throw new scala.Exception("File not found")
      }
      consultQuery.close()
      println(s"prolog file $filePath loaded.")
    }
  }
}