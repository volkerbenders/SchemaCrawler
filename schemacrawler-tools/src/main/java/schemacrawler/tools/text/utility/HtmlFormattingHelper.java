/*
 *
 * SchemaCrawler
 * http://sourceforge.net/projects/schemacrawler
 * Copyright (c) 2000-2015, Sualeh Fatehi.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */

package schemacrawler.tools.text.utility;


import static schemacrawler.tools.text.utility.DatabaseObjectColorMap.getHtmlColor;
import static schemacrawler.tools.text.utility.html.Entities.escapeForXMLElement;
import static sf.util.Utility.isBlank;
import static sf.util.Utility.readResourceFully;

import java.awt.Color;

import schemacrawler.tools.options.TextOutputFormat;

/**
 * Methods to format entire rows of output as HTML.
 *
 * @author Sualeh Fatehi
 */
public final class HtmlFormattingHelper
  extends BaseTextFormattingHelper
{

  private static final String HTML_HEADER = htmlHeader();
  private static final String HTML_FOOTER = "</body>" + System.lineSeparator()
                                            + "</html>";

  private static String htmlHeader()
  {
    final StringBuffer styleSheet = new StringBuffer();
    styleSheet.append(System.lineSeparator())
      .append(readResourceFully("/sc.css")).append(System.lineSeparator())
      .append(readResourceFully("/sc_output.css"))
      .append(System.lineSeparator());

    return "<!DOCTYPE html>" + System.lineSeparator() + "<html lang=\"en\">"
           + System.lineSeparator() + "<head>" + System.lineSeparator()
           + "  <title>SchemaCrawler Output</title>" + System.lineSeparator()
           + "  <meta charset=\"utf-8\"/>" + System.lineSeparator()
           + "  <style>" + styleSheet + "  </style>" + System.lineSeparator()
           + "</head>" + System.lineSeparator() + "<body>"
           + System.lineSeparator();
  }

  public HtmlFormattingHelper(final TextOutputFormat outputFormat)
  {
    super(outputFormat);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String createDocumentEnd()
  {
    return HTML_FOOTER;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String createDocumentStart()
  {
    return HTML_HEADER;
  }

  @Override
  public String createHeader(final DocumentHeaderType type, final String header)
  {
    if (!isBlank(header) && type != null)
    {
      return String.format("%s%n<%s>%s</%s>%n",
                           type.getPrefix(),
                           type.getHeaderTag(),
                           header,
                           type.getHeaderTag());
    }
    else
    {
      return "";
    }
  }

  @Override
  public String createLeftArrow()
  {
    return "\u2190";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String createObjectEnd()
  {
    return "</table>" + System.lineSeparator() + "<p>&#160;</p>"
           + System.lineSeparator();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String createObjectNameRow(final String id,
                                    final String name,
                                    final String description,
                                    final Color backgroundColor)
  {
    final StringBuilder buffer = new StringBuilder();
    buffer.append("  <caption style='background-color: ")
      .append(getHtmlColor(backgroundColor)).append(";'>");
    if (!isBlank(name))
    {
      buffer.append("<span");
      if (!isBlank(id))
      {
        buffer.append(" id='").append(id).append("'");
      }
      buffer.append(" class='caption_name'>").append(escapeForXMLElement(name))
        .append("</span>");
    }
    if (!isBlank(description))
    {
      buffer.append(" <span class='caption_description'>")
        .append(escapeForXMLElement(description)).append("</span>");
    }
    buffer.append("</caption>").append(System.lineSeparator());
    return buffer.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String createObjectStart()
  {
    final StringBuilder buffer = new StringBuilder();
    buffer.append("<table>").append(System.lineSeparator());
    return buffer.toString();
  }

  @Override
  public String createRightArrow()
  {
    return "\u2192";
  }

  @Override
  public String createWeakLeftArrow()
  {
    return "\u21dc";
  }

  @Override
  public String createWeakRightArrow()
  {
    return "\u21dd";
  }

}
