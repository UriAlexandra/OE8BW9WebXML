<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
      xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="html" indent="yes" />

  <xsl:template match="/">
    <html>
      <head>
        <title>Hallgatók adatai</title>
        <meta charset="UTF-8" />
      </head>
      <body>
        <h2>Hallgatók adatai - for-each, value-of</h2>

        <table border="1" cellpadding="5">
          <tr bgcolor="#90EE90">
            <th>ID</th>
            <th>Keresztnév</th>
            <th>Vezetéknév</th>
            <th>Becenév</th>
            <th>Kor</th>
            <th>Ösztöndíj</th>
          </tr>

          <!-- Minden student bejárása -->
          <xsl:for-each select="class/student">
            <tr>
              <td><xsl:value-of select="@id"/></td>
              <td><xsl:value-of select="keresztnev"/></td>
              <td><xsl:value-of select="vezeteknev"/></td>
              <td><xsl:value-of select="becenev"/></td>
              <td><xsl:value-of select="kor"/></td>
              <td><xsl:value-of select="osztondij"/></td>
            </tr>
          </xsl:for-each>

        </table>
      </body>
    </html>
  </xsl:template>

</xsl:stylesheet>
