<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="html" encoding="UTF-8" indent="yes"/>

  <xsl:template match="/">
    <html>
      <head>
        <title>Órarend</title>
      </head>
      <body>
        <h1>Órarend</h1>
        <table border="1">
          <tr>
            <th>ID</th>
            <th>Tárgy</th>
            <th>Típus</th>
            <th>Nap</th>
            <th>Időtartam</th>
            <th>Helyszín</th>
            <th>Oktató</th>
            <th>Szak</th>
          </tr>

          <xsl:for-each select="UAN_orarend/ora">
            <tr>
              <td><xsl:value-of select="@id"/></td>
              <td><xsl:value-of select="targy"/></td>
              <td><xsl:value-of select="@tipus"/></td>
              <td><xsl:value-of select="idopont/nap"/></td>
              <td>
                <xsl:value-of select="idopont/tol"/> -
                <xsl:value-of select="idopont/ig"/>
              </td>
              <td><xsl:value-of select="helyszin"/></td>
              <td><xsl:value-of select="oktato"/></td>
              <td><xsl:value-of select="szak"/></td>
            </tr>
          </xsl:for-each>

        </table>
      </body>
    </html>
  </xsl:template>

</xsl:stylesheet>
