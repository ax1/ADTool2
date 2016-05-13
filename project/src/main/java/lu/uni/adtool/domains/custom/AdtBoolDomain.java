/**
 * Author: Piotr Kordy (piotr.kordy@uni.lu <mailto:piotr.kordy@uni.lu>)
 * Date:   10/12/2015
 * Copyright (c) 2015,2013,2012 University of Luxembourg -- Faculty of Science,
 *     Technology and Communication FSTC
 * All rights reserved.
 * Licensed under GNU Affero General Public License 3.0;
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Affero General Public License as
 *    published by the Free Software Foundation, either version 3 of the
 *    License, or (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package lu.uni.adtool.domains.custom;

import lu.uni.adtool.domains.AdtDomain;
import lu.uni.adtool.domains.adtpredefined.DescriptionGenerator;
import lu.uni.adtool.domains.rings.Bool;
import lu.uni.adtool.tools.Debug;
import lu.uni.adtool.tools.Options;
import lu.uni.adtool.tree.ADTNode;
import lu.uni.adtool.tree.Node;

public class AdtBoolDomain implements AdtDomain<Bool> {
  public AdtBoolDomain () {
    name = Options.getMsg("adtdomain.custom.bool.name");
    description = Options.getMsg("adtdomain.custom.bool.description");
    this.parser = new BoolParser();
  }

  public boolean isValueModifiable(ADTNode node) {
    if(node.getRole() == ADTNode.Role.PROPONENT) {
      return this.proponentModifiable;
    }
    else {
      return this.opponnentModifiable;
    }
  }

  public Bool calc(Bool a, Bool b, ADTNode.Type type) {
    switch (type) {
    case OR_OPP:
      return oo.evaluate(a, b);
    case AND_OPP:
      return ao.evaluate(a, b);
    case OR_PRO:
      return op.evaluate(a, b);
    case AND_PRO:
      return ap.evaluate(a, b);
    default:
      return oo.evaluate(a, b);
    }
  }

  public String getName() {
    return this.name;
  }

  public boolean setName(String name) {
    this.name = name;
    if (name != null && (!name.equals(""))) {
      Debug.log("ok");
      return true;
    }
    Debug.log("not ok");
    return false;
  }

  public String getDescription() {
    return this.description;
  }

  public boolean setDescription(String description) {
    if (description == null || (description.equals(""))) {
      return false;
    }
    String[] operators = { this.op.toUnicode()
                        , this.oo.toUnicode()
                        , this.ap.toUnicode()
                        , this.ao.toUnicode()
                        , this.cp.toUnicode()
                        , this.co.toUnicode()
                         };
    this.description =  DescriptionGenerator.generateDescription(this, description , "{true,&nbsp;false}", operators);
    return true;
  }

  public final Bool cp(final Bool a, final Bool b) {
    return this.cp.evaluate(a, b);
  }

  public final Bool co(final Bool a, final Bool b) {
    return this.co.evaluate(a, b);
  }

  public final Bool getDefaultValue(Node node) {
    if (((ADTNode) node).getRole() == ADTNode.Role.PROPONENT) {
      return proDefValue;
    }
    else {
      return oppDefValue;
    }
  }

  public boolean setCp(String expr) {
    this.cp = this.parser.parseString(expr);
    if (this.cp == null) {
      Debug.log("not ok:\"" + expr + "\"");
      return false;
    }
    //TODO - display error message
    Debug.log("ok");
    return true;
  }

  public boolean setCo(String expr) {
    this.co = this.parser.parseString(expr);
    if (this.co == null) {
      Debug.log("not ok:" + expr);
      return false;
    }
    //TODO - display error message
    Debug.log("ok");
    return true;
  }

  public boolean setAp(String expr) {
    this.ap = this.parser.parseString(expr);
    if (this.ap == null) {
      Debug.log("not ok");
      return false;
    }
    //TODO - display error message
    Debug.log("ok");
    return true;
  }

  public boolean setAo(String expr) {
    this.ao = this.parser.parseString(expr);
    if (this.ao == null) {
      Debug.log("not ok");
      return false;
    }
    //TODO - display error message
    Debug.log("ok");
    return true;
  }

  public boolean setOo(String expr) {
    this.oo = this.parser.parseString(expr);
    if (this.oo == null) {
      Debug.log("not ok");
      return false;
    }
    //TODO - display error message
    Debug.log("ok");
    return true;
  }

  public boolean setOp(String expr) {
    this.op = this.parser.parseString(expr);
    if (this.op == null){
      Debug.log("not ok");
      return false;
    }
    //TODO - display error message
    Debug.log("ok");
    return true;
  }

  public void setOppDefValue(boolean value) {
    this.oppDefValue = new Bool(value);
  }

  public void setProDefValue(boolean value) {
    this.proDefValue = new Bool(value);
  }

  public void setOppModifiable(boolean value) {
    this.opponnentModifiable = value;
  }

  public void setProModifiable(boolean value) {
    this.proponentModifiable = value;
  }

  private Bool oppDefValue;
  private Bool proDefValue;
  private boolean opponnentModifiable;
  private boolean proponentModifiable;
  private String name;
  private String description;
  private BoolExpression cp;
  private BoolExpression co;
  private BoolExpression ao;
  private BoolExpression ap;
  private BoolExpression oo;
  private BoolExpression op;
  private BoolParser parser;
}