# EdiWriter
Java library to write EDIFACT

Java Usage:

        final EDIFACTSettings ediSettings = new EDIFACTSettings();
        ediSettings.setEdiDecimalNotation('.');

        final EdiWriter ediW = new EdiWriter(ediSettings);

        ediW.record(new UNA(ediSettings));

        //
        // -- Create UNB --
        //
        UNB unb = (UNB) ediW.record(new UNB());

        // Create composite S001
        UNB.S001 s001 = unb.new S001();
        s001.SyntaxIdentifier_0001 = "IATB";
        s001.SyntaxVersionNumber_0002 = "1";
        unb.S001.add(s001);

        // Create composite S002
        UNB.S002 s002 = unb.new S002();
        s002.InterchangeSenderIdentification_0004 = "6XPPC";
        unb.S002.add(s002);

        // Create composite S003
        UNB.S003 s003 = unb.new S003();
        s003.InterchangeRecipientIdentification_0010 = "LHPPC";
        unb.S003.add(s003);

        // Create composite S004
        UNB.S004 s004 = unb.new S004();
        s004.Date_0017 = "940101";
        s004.Time_0019 = "0950";
        unb.S004.add(s004);

        unb.InterchangeControlReference_0020 = "1";

        //
        // -- Create UNH --
        //
        UNH unh = (UNH) ediW.record(new UNH());
        unh.MessageReferenceNumber_0062 = "1";

        // Create composite S004
        UNH.S009 s009 = unh.new S009();
        s009.MessageType_0065 = "PAORES";
        s009.MessageVersionNumber_0052 = "93";
        s009.MessageReleaseNumber_0054 = "1";
        s009.ControllingAgencyCoded_0051 = "IA";
        unh.S009.add(s009);

        ediW.record("MSG").composite("1", "45");
        ediW.record("IFT").element("3").element("XYZCOMPANY AVAILABILITY");
        ediW.record("ERC").composite("A7V", "1", "AMD");
        ediW.record("IFT").element("3").element("NO MORE FLIGHTS");
        ediW.record("ODI");

        Record tvl = ediW.record("TVL").composite("240493", "1000", "", "1220");
        tvl.element("FRA").element("JFK").element("DL").element("400").element("C");

        Record pdi = ediW.record("PDI").element("").composite("C", "3").composite("Y", "", "3");
        pdi.composite("F", "", "1");
        ediW.record("APD").composite("74C", "0", "", "", "6").elementRep(5).element("6X");

        Record tvl2 = ediW.record("TVL").composite("240493", "1740", "", "2030").element("JFK");
        tvl2.element("MIA").element("DL").element("081").element("C");
        ediW.record("PDI").element("").composite("C", "4");
        ediW.record("APD").composite("EM2", "0", "1630", "", "6").elementRep(6).element("DA");

        //
        // -- Create UNT --
        //
        UNT unt = new UNT();
        unt.MessageReferenceNumber_0062 = unh.MessageReferenceNumber_0062;
        ediW.record(unt);

        //
        // -- Create UNZ --
        //
        UNZ unz = new UNZ();
        unz.InterchangeControlReference_0020 = unb.InterchangeControlReference_0020;
        ediW.record(unz);

        String ediMsg = ediW.getEdiMessage()
        
        
