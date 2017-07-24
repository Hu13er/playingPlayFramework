package models

import anorm._
import play.api.db.DBApi
import scala.concurrent.Future
import scala.util.Random
import javax.inject.{Inject, Singleton}

case class User(id: Option[Long] = None, username: Option[String] = None, password: Option[String] = None, token: Option[String] = None)

@Singleton
class UserCollection @Inject() (dbs: DBApi)(implicit ec: DatabaseExecutionContext) {

  private val db = dbs.database("default")
  private val parser = Macro.namedParser[User]


  def create(username: String, password: String): Unit = {
      Future {
        val token = Random.nextString(20)
        db.withConnection { implicit conn =>
          SQL("INSERT INTO users(username, password, token) VALUES ({username}, {password}, {token})").
            on('username -> username, 'password -> password, 'token -> token).
            executeInsert()
        }
      }
  }

  def delete(username: String, password: String): Unit = {
    Future {
      db.withConnection { implicit conn =>
        SQL("DELETE FROM useres WHERE username = {username} AND password = {password}").
          on('username -> username, 'password -> password).
          execute()
      }
    }
  }

  def login(username: String, password: String): Future[Option[User]] = {
    Future {
      db.withConnection { implicit conn =>
        SQL("SELECT * FROM users WHERE username = {username} AND password = {password}").
          on('username -> username, 'password -> password).
          executeQuery().as(parser.*).headOption
      }
    }
  }

  def findByToken(token: String): Future[Option[User]] = {
    Future {
      db.withConnection { implicit conn =>
        SQL("SELECT * FROM users WHERE token = {token}").
          on('token -> token).
          executeQuery().as(parser.*).headOption
      }
    }
  }

}
