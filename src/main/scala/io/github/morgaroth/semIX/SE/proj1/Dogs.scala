package io.github.morgaroth.semIX.SE.proj1

import java.io.File

import jpl._

import scala.io.Source
import scala.swing.BorderPanel.Position
import scala.swing._
import scala.swing.event.ButtonClicked

/**
  * Created by mateusz on 22.11.15.
  */

object Dogs extends SimpleSwingApplication {
  def top = new MainFrame {
    val questionLabel = new Label("HERE IS QUESTION")
    val yesButton = new Button("YES")
    val noButton = new Button("NO")
    val restart = new Button("START")
    val proposition = new Label("HERE IS PROPOSTION")
    val loadBtn = new Button("Load prolog file")
    val code = new TextArea()

    title = "Dog"
    preferredSize = new Dimension(600, 600)
    resizable = true
    contents = new BorderPanel {
      add(new BorderPanel() {
        add(new GridPanel(6, 1) {
          contents += new Label("Odpowiedz na pytanie:")
          contents += questionLabel
          contents += new GridPanel(1, 2) {
            contents += yesButton
            contents += noButton
          }
          contents += new Label("Twoja propozycja to:")
          contents += proposition
          contents += restart
        }, Position.North)
      }, BorderPanel.Position.West)
      add(new BorderPanel {
        add(new Label("Prolog code:"), Position.North)
        add(new ScrollPane(code), Position.Center)
        add(loadBtn, Position.South)
      }, Position.Center)
    }
    listenTo(yesButton, noButton, loadBtn)

    reactions += {
      case ButtonClicked(`noButton`) => println("no clicked")
      case ButtonClicked(`yesButton`) => println("yes clicked")
      case ButtonClicked(`restart`)=>
        println("start clicked")
        restart.text = "Restart"
        reloadGame()
      case ButtonClicked(`loadBtn`) =>
        println("load file clicked")
        loadProlog
    }

    def reloadGame(): Unit = {
      JPL.init()
      val consultQuery = new Query("consult", Array[Term](new Atom("prolog.pl")))
      if (!consultQuery.hasSolution) {
        throw new Exception("File not found")
      }
      consultQuery.close()
    }

    def loadProlog = {
      val jc = new FileChooser(new File("~/"))
      val r = jc.showOpenDialog(null)
      if (r == FileChooser.Result.Approve) {
        println(s"selected ${jc.selectedFile.getAbsolutePath}")
        code.text = Source.fromFile(jc.selectedFile).mkString
      }
    }

  }
}