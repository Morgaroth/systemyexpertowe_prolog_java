package io.github.morgaroth.semIX.SE.proj1

/**
  * Created by mateusz on 24.11.15.
  */
object BRIDGE {

  private var list: Questions = null

  def setListener(s: Questions) = {
    list = s
  }

  def ask(question: String): String = {
    println(s"ask from prolog with $question")
    if (list.ask(question)) "t" else "n"
  }

  def result(a: String) {
    println(s"got result $a")
    list.result(a)
  }
}


trait Questions {
  def result(preposition: String): Unit

  def ask(question: String): Boolean
}