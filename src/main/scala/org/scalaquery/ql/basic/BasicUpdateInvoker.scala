package org.scalaquery.ql.basic

import org.scalaquery.ql.{Query, Projection, ColumnBase}
import org.scalaquery.session.{Session, ReadAheadIterator, PositionedParameters}
import org.scalaquery.util.{CloseableIterator, NamingContext}

class BasicUpdateInvoker[T] (query: Query[ColumnBase[T]], profile: BasicProfile) {

  protected lazy val built = profile.buildUpdateStatement(query, NamingContext())

  def updateStatement = getStatement

  protected def getStatement = built.sql

  def update(value: T)(implicit session: Session): Int = session.withPreparedStatement(updateStatement) { st =>
    st.clearParameters
    val pp = new PositionedParameters(st)
    query.value.setParameter(profile, pp, Some(value))
    built.setter(pp, null)
    st.executeUpdate
  }

  def updateInvoker: this.type = this
}