package controllers

import javax.inject.Inject

import models._
import play.api.libs.json.JsValue
import play.api.mvc._
import views._

import scala.concurrent.{ExecutionContext, Future}

/**
  * Manage a database of computers
  */
class HomeController @Inject()(userCollection: UserCollection,
                               cc: ControllerComponents)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  def index = Action.async { implicit request: Request[AnyContent] =>
    request.session.get("token") match {
      case Some(token) =>
            userCollection.findByToken(token).map { maybeUser =>
            maybeUser.
              map(user => Ok("Hello " + user.username.getOrElse(""))).
              getOrElse(BadRequest("Not found."))
          }
      case None => Future(Ok("Hello anonymouse ^.^"))
    }
  }

  def login = Action.async(parse.json) { implicit request: Request[JsValue] =>
    val (username, password) = (request.body \ "username", request.body \ "password")

    userCollection.login(username.as[String], password.as[String]).map { maybeUesr =>
      maybeUesr.
        map(user => Ok("Logged in " + username).withSession("token" -> user.token.getOrElse(""))).
        getOrElse(BadRequest("invalid username or password."))
    }
  }

  def create = Action(parse.json) { implicit request: Request[JsValue] =>
    val (username, password) = (request.body \ "username", request.body \ "password")
    userCollection.create(username.as[String], password.as[String])
    Ok("User created. now you can login.")
  }

  def delete = Action(parse.json) { implicit request: Request[JsValue] =>
    val (username, password) = (request.body \ "username", request.body \ "password")
    userCollection.delete(username.as[String], password.as[String])
    Ok("User deleted.")
  }
}
            
