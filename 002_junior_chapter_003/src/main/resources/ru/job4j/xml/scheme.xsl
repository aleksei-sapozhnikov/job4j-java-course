<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:xls="http://www.w3.org/1999/XSL/Transform" version="1.0"
>
    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="/">
        <xsl:text>&#10;</xsl:text>
        <entries>
            <xsl:for-each select="/entries/entry">
                <xsl:text>&#10;&#160;&#160;&#160;&#160;</xsl:text>
                <entry>
                    <xls:attribute name="field">
                        <xsl:value-of select="field"/>
                    </xls:attribute>
                </entry>
            </xsl:for-each>
        </entries>
    </xsl:template>

</xsl:stylesheet>