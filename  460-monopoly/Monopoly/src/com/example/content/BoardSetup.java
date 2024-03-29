package com.example.content;

import com.example.model.Tile;
import com.example.monopoly.R;

public class BoardSetup {
	public static void setupBoard(){
		
		//create central circle

		Tile start = new Tile(3, 0, Tile.OWNER_UNOWNABLE);
		start.setName("Pond");
		start.setPrice(420);
		start.setRegion(Tile.REGION_NON_BUYABLE);
		start.setBaseRent(84);
		start.setBaseRegionRent(42);
		start.priceUpgrades(315, 630, 945, 1260);
		start.image = R.drawable.i0;
		
		// Tree
		
		Tile Spruce = start.addNextStop(Tile.DIRECTION_EAST);
		Spruce.setName("Spruce Hall");
		Spruce.setPrice(50);
		Spruce.setRegion(Tile.REGION_TREES);
		Spruce.setBaseRent(5);
		Spruce.setBaseRegionRent(10);
		Spruce.priceUpgrades(37.5, 75, 112.5, 150);
		Spruce.image = R.drawable.i1;
		
		Tile Oak = Spruce.addNextStop(Tile.DIRECTION_EAST);
		Oak.setName("Oak Hall");
		Oak.setPrice(50);
		Oak.setRegion(Tile.REGION_TREES);
		Oak.setBaseRent(5);
		Oak.setBaseRegionRent(10);
		Oak.priceUpgrades(37.5, 75, 112.5, 150);
		Oak.image = R.drawable.i2;
		
		Tile Maple = Oak.addNextStop(Tile.DIRECTION_EAST);
		Maple.setName("Maple Hall");
		Maple.setPrice(50);
		Maple.setRegion(Tile.REGION_TREES);
		Maple.setBaseRent(5);
		Maple.setBaseRegionRent(10);
		Maple.priceUpgrades(37.5, 75, 112.5, 150);
		Maple.image = R.drawable.i3;
		
		Tile Elm = Maple.addNextStop(Tile.DIRECTION_EAST);
		Elm.setName("Elm Hall");
		Elm.setPrice(50);
		Elm.setRegion(Tile.REGION_TREES);
		Elm.setBaseRent(5);
		Elm.setBaseRegionRent(10);
		Elm.priceUpgrades(37.5, 75, 112.5, 150);
		Elm.image = R.drawable.i4;
		
		Tile Cedar = Elm.addNextStop(Tile.DIRECTION_EAST);
		Cedar.setName("Cedar Hall");
		Cedar.setRegion(Tile.REGION_TREES);
		Cedar.setPrice(50);
		Cedar.setBaseRent(5);
		Cedar.setBaseRegionRent(10);
		Cedar.priceUpgrades(37.5, 75, 112.5, 150);
		Cedar.image = R.drawable.i5;
		
		Tile Birch = Cedar.addNextStop(Tile.DIRECTION_EAST);
		Birch.setName("Birch Hall");
		Birch.setRegion(Tile.REGION_TREES);
		Birch.setPrice(50);
		Birch.setBaseRent(5);
		Birch.setBaseRegionRent(10);
		Birch.priceUpgrades(37.5, 75, 112.5, 150);
		Birch.image = R.drawable.i6;
		
		Tile Alder = Birch.addNextStop(Tile.DIRECTION_EAST);
		Alder.setName("Alder Hall");
		Alder.setRegion(Tile.REGION_TREES);
		Alder.setPrice(50);
		Alder.setBaseRent(5);
		Alder.setBaseRegionRent(10);
		Alder.priceUpgrades(37.5, 75, 112.5, 150);
		Alder.image = R.drawable.i7;
		
		//TOP EAST
		
		Tile Slade = Alder.addNextStop(Tile.DIRECTION_SOUTHEAST);
		Slade.setName("Slade Hall");
		Slade.setRegion(Tile.REGION_FRESHMAN);
		Slade.setPrice(50);
		Slade.setBaseRent(10);
		Slade.setBaseRegionRent(5);
		Slade.priceUpgrades(37.5, 75, 112.5, 150);
		Slade.image = R.drawable.i8;
		
		Tile Shuttle = Slade.addNextStop(Tile.DIRECTION_SOUTHEAST);
		Shuttle.setName("Tree Dorm Shuttle Stop");
		Shuttle.setRegion(Tile.REGION_SHUTTLE);
		Shuttle.setPrice(250);
		Shuttle.setBaseRent(50);
		Shuttle.setBaseRegionRent(25);
		Shuttle.priceUpgrades(187.5, 375, 562.5, 750);
		Shuttle.image = R.drawable.i9;
		
		Tile Miller = Shuttle.addNextStop(Tile.DIRECTION_SOUTHEAST);
		Miller.setName("Miller Hall");
		Miller.setRegion(Tile.REGION_FRESHMAN);
		Miller.setPrice(50);
		Miller.setBaseRent(10);
		Miller.setBaseRegionRent(5);
		Miller.priceUpgrades(37.5, 75, 112.5, 150);
		Miller.image = R.drawable.i10;
		
		Tile Seasons = Miller.addNextStop(Tile.DIRECTION_SOUTHEAST);
		Seasons.setName("Seasons Dining Hall");
		Seasons.setRegion(Tile.REGION_STUDENT_CENTER);
		Seasons.setPrice(200);
		Seasons.setBaseRent(40);
		Seasons.setBaseRegionRent(20);
		Seasons.priceUpgrades(150, 300, 450, 600);
		Seasons.image = R.drawable.i11;
		
		// South West
		
		Tile BackBay = Seasons.addNextStop(Tile.DIRECTION_SOUTHWEST);
		BackBay.setName("Back Bay");
		BackBay.setRegion(Tile.REGION_STUDENT_CENTER);
		BackBay.setPrice(200);
		BackBay.setBaseRent(40);
		BackBay.setBaseRegionRent(20);
		BackBay.priceUpgrades(150, 300, 450, 600);
		BackBay.image = R.drawable.i12;
		
		Tile Harry = BackBay.addNextStop(Tile.DIRECTION_SOUTHWEST);
		Harry.setName("Harry's Pub");
		Harry.setRegion(Tile.REGION_STUDENT_CENTER);
		Harry.setPrice(200);
		Harry.setBaseRent(40);
		Harry.setBaseRegionRent(20);
		Harry.priceUpgrades(150, 300, 450, 600);
		Harry.image = R.drawable.i13;
		
		Tile MadFalcon = Harry.addNextStop(Tile.DIRECTION_SOUTHWEST);
		MadFalcon.setName("Mad Falcon");
		MadFalcon.setRegion(Tile.REGION_EATERIES);
		MadFalcon.setPrice(250);
		MadFalcon.setBaseRent(50);
		MadFalcon.setBaseRegionRent(25);
		MadFalcon.priceUpgrades(187.5, 375, 562.5, 750);
		MadFalcon.image = R.drawable.i14;
		
		//South East
		
		Tile Health = MadFalcon.addNextStop(Tile.DIRECTION_SOUTHEAST);
		Health.setName("Health & Wellness Center");
		Health.setRegion(Tile.REGION_SPECIAL);
		Health.setPrice(300);
		Health.setBaseRent(60);
		Health.setBaseRegionRent(30);
		Health.priceUpgrades(225, 450, 675, 900);
		Health.setOwner(Tile.OWNER_UNOWNABLE);
		Health.image = R.drawable.i15;
		
		Tile.setClinicTile(Health);
		
		                            //green space...............
		
		Tile Greenspace = Health.addNextStop(Tile.DIRECTION_SOUTHEAST);
		Greenspace.setName("Greenspace");
		Greenspace.setPrice(420);
		Greenspace.setRegion(Tile.REGION_NON_BUYABLE);
		Greenspace.setBaseRent(84);
		Greenspace.setBaseRegionRent(42);
		Greenspace.priceUpgrades(315, 630, 945, 1260);
		Greenspace.setOwner(Tile.OWNER_UNOWNABLE);
		Greenspace.image = R.drawable.i16;
		
		//South West
		
		Tile Boyston = Greenspace.addNextStop(Tile.DIRECTION_SOUTHWEST);
		Boyston.setName("Boylston Apartments");
		Boyston.setPrice(300);
		Boyston.setRegion(Tile.REGION_SENIAL_ART);
		Boyston.setBaseRent(60);
		Boyston.setBaseRegionRent(30);
		Boyston.priceUpgrades(225, 450, 675, 900);
		Boyston.image = R.drawable.i17;
		
		
		Tile Rhodes = Boyston.addNextStop(Tile.DIRECTION_SOUTHWEST);
		Rhodes.setName("Rhodes Hall");
		Rhodes.setPrice(300);
		Rhodes.setRegion(Tile.REGION_SENIAL_ART);
		Rhodes.setBaseRent(60);
		Rhodes.setBaseRegionRent(30);
		Rhodes.priceUpgrades(225, 450, 675, 900);
		Rhodes.image = R.drawable.i18;
		
		Tile Donut = Rhodes.addNextStop(Tile.DIRECTION_SOUTHWEST);
		Donut.setName("Donut Shop");
		Donut.setRegion(Tile.REGION_EATERIES);
		Donut.setPrice(250);
		Donut.setBaseRent(50);
		Donut.setBaseRegionRent(25);
		Donut.priceUpgrades(187.5, 375, 562.5, 750);
		Donut.image = R.drawable.i19;
		
		
		Tile Collins = Donut.addNextStop(Tile.DIRECTION_SOUTHWEST);
		Collins.setName("Collins Hall");
		Collins.setPrice(300);
		Collins.setRegion(Tile.REGION_SENIAL_ART);
		Collins.setBaseRent(60);
		Collins.setBaseRegionRent(30);
		Collins.priceUpgrades(225, 450, 675, 900);
		Collins.image = R.drawable.i20;
		
		Tile Road1 = Collins.addNextStop(Tile.DIRECTION_SOUTHWEST);
		Road1.setName("Forest Street Bridge - Kresge Pathway");
		Road1.setRegion(Tile.REGION_PATHWAYS);
		Road1.setPrice(50);
		Road1.setBaseRent(50);
		Road1.setBaseRegionRent(50);
		Road1.priceUpgrades(200, 300, 500, 700);
		Road1.setOwner(Tile.OWNER_UNOWNABLE);
		Road1.image = R.drawable.i21;
		
		//West 
		
		Tile Kresge = Road1.addNextStop(Tile.DIRECTION_WEST);
		Kresge.setName("Kresge Hall");
		Kresge.setRegion(Tile.REGION_SOPHMORE_SUITES);
		Kresge.setPrice(200);
		Kresge.setBaseRent(40);
		Kresge.setBaseRegionRent(20);
		Kresge.priceUpgrades(150, 300, 450, 600);
		Kresge.image = R.drawable.i22;
		
		Tile Forest = Kresge.addNextStop(Tile.DIRECTION_WEST);
		Forest.setName("Forest Hall");
		Forest.setRegion(Tile.REGION_SOPHMORE_SUITES);
		Forest.setPrice(200);
		Forest.setBaseRent(40);
		Forest.setBaseRegionRent(20);
		Forest.priceUpgrades(150, 300, 450, 600);
		Forest.image = R.drawable.i23;
		
		//North West
		
		Tile Falcone_west = Forest.addNextStop(Tile.DIRECTION_NORTHWEST);
		Falcone_west.setName("Falcone West");
		Falcone_west.setRegion(Tile.REGION_FALCONE_ART);
		Falcone_west.setPrice(350);
		Falcone_west.setBaseRent(70);
		Falcone_west.setBaseRegionRent(35);
		Falcone_west.priceUpgrades(262.5, 525, 787.5, 1050);
		Falcone_west.image = R.drawable.i24;
		
		Tile Falcone_East = Falcone_west.addNextStop(Tile.DIRECTION_NORTHWEST);
		Falcone_East.setName("Falcone East");
		Falcone_East.setRegion(Tile.REGION_FALCONE_ART);
		Falcone_East.setPrice(350);
		Falcone_East.setBaseRent(70);
		Falcone_East.setBaseRegionRent(35);
		Falcone_East.priceUpgrades(262.5, 525, 787.5, 1050);
		Falcone_East.image = R.drawable.i25;
		
		Tile Falcone_North = Falcone_East.addNextStop(Tile.DIRECTION_NORTHWEST);
		Falcone_North.setName("Falcone North");
		Falcone_North.setRegion(Tile.REGION_FALCONE_ART);
		Falcone_North.setPrice(350);
		Falcone_North.setBaseRent(70);
		Falcone_North.setBaseRegionRent(35);
		Falcone_North.priceUpgrades(262.5, 525, 787.5, 1050);
		Falcone_North.image = R.drawable.i26;
		
		
		Tile Rauch = Falcone_North.addNextStop(Tile.DIRECTION_NORTHWEST);
		Rauch.setName("Rauch Administrative Center");
		Rauch.setRegion(Tile.REGION_UPPER_CAMPUS_CLASSIC);
		Rauch.setPrice(400);
		Rauch.setBaseRent(80);
		Rauch.setBaseRegionRent(40);
		Rauch.priceUpgrades(300, 600, 900, 1200);
		Rauch.image = R.drawable.i27;
		
		Tile LaCava = Rauch.addNextStop(Tile.DIRECTION_NORTHWEST);
		LaCava.setName("LaCava Center");
		LaCava.setRegion(Tile.REGION_UPPER_CAMPUS_CLASSIC);
		LaCava.setPrice(400);
		LaCava.setBaseRent(80);
		LaCava.setBaseRegionRent(40);
		LaCava.priceUpgrades(300, 600, 900, 1200);
		LaCava.image = R.drawable.i28;
		
		Tile LaCava_Shuttle = LaCava.addNextStop(Tile.DIRECTION_NORTHWEST);
		LaCava_Shuttle.setName("LaCava Shuttle Stop");
		LaCava_Shuttle.setRegion(Tile.REGION_SHUTTLE);
		LaCava_Shuttle.setPrice(250);
		LaCava_Shuttle.setBaseRent(50);
		LaCava_Shuttle.setBaseRegionRent(25);
		LaCava_Shuttle.priceUpgrades(187.5, 375, 562.5, 750);
		LaCava_Shuttle.image = R.drawable.i29;
		
		Tile Jenision = LaCava_Shuttle.addNextStop(Tile.DIRECTION_NORTHWEST);
		Jenision.setName("Jennison Hall");
		Jenision.setRegion(Tile.REGION_UPPER_CAMPUS_CLASSIC);
		Jenision.setPrice(400);
		Jenision.setBaseRent(80);
		Jenision.setBaseRegionRent(40);
		Jenision.priceUpgrades(300, 600, 900, 1200);
		Jenision.image = R.drawable.i30;
		
		//North East
		
		Tile Lindsay = Jenision.addNextStop(Tile.DIRECTION_NORTHEAST);
		Lindsay.setName("Lindsay Hall");
		Lindsay.setRegion(Tile.REGION_UPPER_CAMPUS_MODERN);
		Lindsay.setPrice(450);
		Lindsay.setBaseRent(90);
		Lindsay.setBaseRegionRent(45);
		Lindsay.priceUpgrades(337.5, 675, 1012.5, 1350);
		Lindsay.image = R.drawable.i31;
		
		//North West
		
		Tile SATC = Lindsay.addNextStop(Tile.DIRECTION_NORTHWEST);
		SATC.setName("Smith Academic Technology Center");
		SATC.setRegion(Tile.REGION_UPPER_CAMPUS_MODERN);
		SATC.setPrice(450);
		SATC.setBaseRent(90);
		SATC.setBaseRegionRent(45);
		SATC.priceUpgrades(337.5, 675, 1012.5, 1350);
		SATC.image = R.drawable.i32;
		
		/*Tile Morrison = SATC.addNextStop(Tile.DIRECTION_NORTHWEST);
		Morrison.setName("Morrison Hall");
		Morrison.setRegion(Tile.REGION_UPPER_CAMPUS_MODERN);
		Morrison.setPrice(450);
		Morrison.setBaseRent(90);
		Morrison.setBaseRegionRent(45);
		Morrison.priceUpgrades(337.5, 675, 1012.5, 1350);*/
		
		Tile AAC = SATC.addNextStop(Tile.DIRECTION_NORTHWEST);
		AAC.setName("Adamian Academic Center");
		AAC.setRegion(Tile.REGION_UPPER_CAMPUS_MODERN);
		AAC.setPrice(450);
		AAC.setBaseRent(90);
		AAC.setBaseRegionRent(45);
		AAC.priceUpgrades(337.5, 675, 1012.5, 1350);
		AAC.image = R.drawable.i33;
		
		Tile Bagel = AAC.addNextStop(Tile.DIRECTION_NORTHWEST);
		Bagel.setName("Bagel Shop");
		Bagel.setRegion(Tile.REGION_EATERIES);
		Bagel.setPrice(250);
		Bagel.setBaseRent(50);
		Bagel.setBaseRegionRent(25);
		Bagel.priceUpgrades(187.5, 375, 562.5, 750);
		Bagel.image = R.drawable.i34;
		
		Tile Baker = Bagel.addNextStop(Tile.DIRECTION_NORTHWEST);
		Baker.setName("Baker Library");
		Baker.setRegion(Tile.REGION_POSSESSIONS);
		Baker.setPrice(500);
		Baker.setBaseRent(100);
		Baker.setBaseRegionRent(50);
		Baker.priceUpgrades(375, 750, 1125, 1500);
		Baker.image = R.drawable.i35;

		Tile Falcon_Statue = Baker.addNextStop(Tile.DIRECTION_NORTHWEST);
		Falcon_Statue.setName("Falcon Statue");
		Falcon_Statue.setRegion(Tile.REGION_POSSESSIONS);
		Falcon_Statue.setPrice(500);
		Falcon_Statue.setBaseRent(100);
		Falcon_Statue.setBaseRegionRent(50);
		Falcon_Statue.priceUpgrades(375, 750, 1125, 1500);
		Falcon_Statue.image = R.drawable.i36;

		//  North East
		
		Tile Presidential = Falcon_Statue.addNextStop(Tile.DIRECTION_NORTHEAST);
		Presidential.setName("Presidential Villa");
		Presidential.setRegion(Tile.REGION_POSSESSIONS);
		Presidential.setPrice(500);
		Presidential.setBaseRent(100);
		Presidential.setBaseRegionRent(50);
		Presidential.priceUpgrades(375, 750, 1125, 1500);
		Presidential.addNextStop(start);
		Presidential.image = R.drawable.i37;
		
		//main root end
        //South East
		Tile Road2 = Road1.addNextStop(Tile.DIRECTION_SOUTHEAST);
		Road2.setName("Forest Street Bridge - Mid-Kresge Pathway");
		Road2.setRegion(Tile.REGION_PATHWAYS);
		Road2.setPrice(50);
		Road2.setBaseRent(50);
		Road2.setBaseRegionRent(50);
		Road2.priceUpgrades(200, 300, 500, 700);
		Road2.addNextStop(Road1);
		Road2.setOwner(Tile.OWNER_UNOWNABLE);
		Road2.image = R.drawable.i38;

		//East
		
		Tile Road3 = Road2.addNextStop(Tile.DIRECTION_EAST);
		Road3.setName("Forest Street Bridge - Kresge-Side Stairs");
		Road3.setRegion(Tile.REGION_PATHWAYS);
		Road3.setPrice(50);
		Road3.setBaseRent(50);
		Road3.setBaseRegionRent(50);
		Road3.priceUpgrades(200, 300, 500, 700);
		Road3.addNextStop(Road2);
		Road3.setOwner(Tile.OWNER_UNOWNABLE);
		Road3.image = R.drawable.i39;

		
		Tile Road4 = Road3.addNextStop(Tile.DIRECTION_EAST);
		Road4.setName("Forest Street Bridge - Kresge-Side Bridge Section");
		Road4.setRegion(Tile.REGION_PATHWAYS);
		Road4.setPrice(50);
		Road4.setBaseRent(50);
		Road4.setBaseRegionRent(50);
		Road4.priceUpgrades(200, 300, 500, 700);
		Road4.addNextStop(Road3);
		Road4.setOwner(Tile.OWNER_UNOWNABLE);
		Road4.image = R.drawable.i40;

		Tile Road5 = Road4.addNextStop(Tile.DIRECTION_EAST);
		Road5.setName("Forest Street Bridge - Middle Bridge Section");
		Road5.setRegion(Tile.REGION_PATHWAYS);
		Road5.setPrice(50);
		Road5.setBaseRent(50);
		Road5.setBaseRegionRent(50);
		Road5.priceUpgrades(200, 300, 500, 700);
		Road5.addNextStop(Road4);
		Road5.setOwner(Tile.OWNER_UNOWNABLE);
		Road5.image = R.drawable.i41;

		Tile Road6 = Road5.addNextStop(Tile.DIRECTION_EAST);
		Road6.setName("Forest Street Bridge - Lower Campus-Side Bridge Section");
		Road6.setRegion(Tile.REGION_PATHWAYS);
		Road6.setPrice(50);
		Road6.setBaseRent(50);
		Road6.setBaseRegionRent(50);
		Road6.priceUpgrades(200, 300, 500, 700);
		Road6.addNextStop(Road5);
		Road6.setOwner(Tile.OWNER_UNOWNABLE);
		Road6.image = R.drawable.i42;

		
		Tile Road7 = Road6.addNextStop(Tile.DIRECTION_EAST);
		Road7.setName("Forest Street Bridge - Lower Campus-Side Stairs");
		Road7.setRegion(Tile.REGION_PATHWAYS);
		Road7.setPrice(50);
		Road7.setBaseRent(50);
		Road7.setBaseRegionRent(50);
		Road7.priceUpgrades(200, 300, 500, 700);
		Road7.addNextStop(Road6);
		Road7.setOwner(Tile.OWNER_UNOWNABLE);
		Road7.image = R.drawable.i43;

		
		Tile Road8 = Road7.addNextStop(Tile.DIRECTION_EAST);
		Road8.setName("Forest Street Bridge - Mid-Lower Campus Pathway");
		Road8.setRegion(Tile.REGION_PATHWAYS);
		Road8.setPrice(50);
		Road8.setBaseRent(50);
		Road8.setBaseRegionRent(50);
		Road8.priceUpgrades(200, 300, 500, 700);
		Road8.addNextStop(Road7);
		Road8.setOwner(Tile.OWNER_UNOWNABLE);
		Road8.image = R.drawable.i44;

		
		//North East
		
		Tile Road9 = Road8.addNextStop(Tile.DIRECTION_NORTHEAST);
		Road9.setName("Forest Street Bridge - Lower Campus Pathway");
		Road9.setRegion(Tile.REGION_PATHWAYS);
		Road9.setPrice(50);
		Road9.setBaseRent(50);
		Road9.setBaseRegionRent(50);
		Road9.priceUpgrades(200, 300, 500, 700);
		Road9.addNextStop(Road8);
		Road9.setOwner(Tile.OWNER_UNOWNABLE);
		Road9.image = R.drawable.i45;

		
		//Lowe_Campus_Greenspase
	
		Tile Lowe_Campus = Road9.addNextStop(Tile.DIRECTION_NORTHEAST);
		Lowe_Campus.setName("Lower Campus Greenspace");
		Lowe_Campus.setPrice(420);
		Lowe_Campus.setRegion(Tile.REGION_NON_BUYABLE);
		Lowe_Campus.setBaseRent(84);
		Lowe_Campus.setBaseRegionRent(42);
		Lowe_Campus.priceUpgrades(315, 630, 945, 1260);
		Lowe_Campus.addNextStop(Road9);
		Lowe_Campus.setOwner(Tile.OWNER_UNOWNABLE);
		Lowe_Campus.image = R.drawable.i46;

		
		//Dana Center
		//East
		
		Tile Swimming = Lowe_Campus.addNextStop(Tile.DIRECTION_EAST);
		Swimming.setName("Swimming Pool");
		Swimming.setPrice(300);
		Swimming.setRegion(Tile.REGION_DANA_CENTER);
		Swimming.setBaseRent(60);
		Swimming.setBaseRegionRent(30);
		Swimming.priceUpgrades(225, 450, 675, 900);
		Swimming.image = R.drawable.i47;
		
		Tile Fitness = Swimming.addNextStop(Tile.DIRECTION_EAST);
		Fitness.setName("Fitness Center");
		Fitness.setPrice(300);
		Fitness.setRegion(Tile.REGION_DANA_CENTER);
		Fitness.setBaseRent(60);
		Fitness.setBaseRegionRent(30);
		Fitness.priceUpgrades(225, 450, 675, 900);
		Fitness.image = R.drawable.i48;
		
		Tile Gymnasium = Fitness.addNextStop(Tile.DIRECTION_EAST);
		Gymnasium.setName("Gymnasium");
		Gymnasium.setPrice(300);
		Gymnasium.setRegion(Tile.REGION_DANA_CENTER);
		Gymnasium.setBaseRent(60);
		Gymnasium.setBaseRegionRent(30);
		Gymnasium.priceUpgrades(225, 450, 675, 900);
		Gymnasium.image = R.drawable.i49;
		

        Tile Burrito = Gymnasium.addNextStop(Tile.DIRECTION_EAST);
        Burrito.setName("Burrito Shop");
        Burrito.setRegion(Tile.REGION_EATERIES);
        Burrito.setPrice(250);
        Burrito.setBaseRent(50);
        Burrito.setBaseRegionRent(25);
        Burrito.priceUpgrades(187.5, 375, 562.5, 750);
        Burrito.image = R.drawable.i50;
        
        Tile Dana_Shuttle = Burrito.addNextStop(Tile.DIRECTION_EAST);
        Dana_Shuttle.setName("Dana Center Shuttle Stop");
        Dana_Shuttle.setRegion(Tile.REGION_SHUTTLE);
        Dana_Shuttle.setPrice(250);
        Dana_Shuttle.setBaseRent(50);
        Dana_Shuttle.setBaseRegionRent(25);
        Dana_Shuttle.priceUpgrades(187.5, 375, 562.5, 750);
        Dana_Shuttle.image = R.drawable.i51;
        
        Tile Athletic1 = Dana_Shuttle.addNextStop(Tile.DIRECTION_EAST);
        Athletic1.setName("Baseball Field");
        Athletic1.setRegion(Tile.REGION_ATHLETIC_COMPLEX);
        Athletic1.setPrice(250);
        Athletic1.setBaseRent(50);
        Athletic1.setBaseRegionRent(25);
        Athletic1.priceUpgrades(187.5, 375, 562.5, 750);
        Athletic1.image = R.drawable.i52;
        
        //North East
        
        Tile Athletic2 = Athletic1.addNextStop(Tile.DIRECTION_NORTHEAST);
        Athletic2.setName("Softball Field");
        Athletic2.setRegion(Tile.REGION_ATHLETIC_COMPLEX);
        Athletic2.setPrice(250);
        Athletic2.setBaseRent(50);
        Athletic2.setBaseRegionRent(25);
        Athletic2.priceUpgrades(187.5, 375, 562.5, 750);
        Athletic2.image = R.drawable.i53;
        
        //North West
        
        Tile Facilities_Management = Athletic2.addNextStop(Tile.DIRECTION_NORTHWEST);
        Facilities_Management.setName("Facilities Management");
        Facilities_Management.setRegion(Tile.REGION_FACILITIES_MANAGEMENT);
        Facilities_Management.setPrice(300);
        Facilities_Management.setBaseRent(60);
        Facilities_Management.setBaseRegionRent(30);
        Facilities_Management.priceUpgrades(225, 450, 675, 900);
        Facilities_Management.image = R.drawable.i54;
        
        // parking space right side
        //North East
        
        Tile Parking1 = Facilities_Management.addNextStop(Tile.DIRECTION_NORTHEAST);
        Parking1.setName("Parking Garage Level 1 - Section A");
        Parking1.setRegion(Tile.REGION_PARKING_SPACES);
        Parking1.setPrice(300);
        Parking1.setBaseRent(60);
        Parking1.setBaseRegionRent(30);
        Parking1.priceUpgrades(200, 300, 400, 500);
        Parking1.setOwner(Tile.OWNER_UNOWNABLE);
        Parking1.image = R.drawable.i55;
        
        //North West
        
        Tile Parking2 = Parking1.addNextStop(Tile.DIRECTION_NORTHWEST);
        Parking2.setName("Parking Garage Level 1 - Section B");
        Parking2.setRegion(Tile.REGION_PARKING_SPACES);
        Parking2.setPrice(300);
        Parking2.setBaseRent(60);
        Parking2.setBaseRegionRent(30);
        Parking2.priceUpgrades(200, 300, 400, 500);
        Parking2.setOwner(Tile.OWNER_UNOWNABLE);
        Parking2.image = R.drawable.i56;
        
        Tile Parking3 = Parking2.addNextStop(Tile.DIRECTION_NORTHWEST);
        Parking3.setName("Parking Garage Level 1 - Section C");
        Parking3.setRegion(Tile.REGION_PARKING_SPACES);
        Parking3.setPrice(300);
        Parking3.setBaseRent(60);
        Parking3.setBaseRegionRent(30);
        Parking3.priceUpgrades(200, 300, 400, 500);
        Parking3.setOwner(Tile.OWNER_UNOWNABLE);
        Parking3.image = R.drawable.i57;
        
        Tile Parking4 = Parking3.addNextStop(Tile.DIRECTION_NORTHWEST);
        Parking4.setName("Parking Garage Level 1 - Section D");
        Parking4.setRegion(Tile.REGION_PARKING_SPACES);
        Parking4.setPrice(300);
        Parking4.setBaseRent(60);
        Parking4.setBaseRegionRent(30);
        Parking4.priceUpgrades(200, 300, 400, 500);
        Parking4.setOwner(Tile.OWNER_UNOWNABLE);
        Parking4.image = R.drawable.i58;
        
        Tile Parking5 = Parking4.addNextStop(Tile.DIRECTION_NORTHWEST);
        Parking5.setName("Parking Garage Level 1 - Section E");
        Parking5.setRegion(Tile.REGION_PARKING_SPACES);
        Parking5.setPrice(300);
        Parking5.setBaseRent(60);
        Parking5.setBaseRegionRent(30);
        Parking5.priceUpgrades(200, 300, 400, 500);
        Parking5.setOwner(Tile.OWNER_UNOWNABLE);
        Parking5.image = R.drawable.i59;
        
        // parking space left side
        
        Tile Parking6 = Facilities_Management.addNextStop(Tile.DIRECTION_WEST);
        Parking6.setName("Parking Garage Level 2 - Section A");
        Parking6.setRegion(Tile.REGION_PARKING_SPACES);
        Parking6.setPrice(300);
        Parking6.setBaseRent(60);
        Parking6.setBaseRegionRent(30);
        Parking6.priceUpgrades(200, 300, 400, 500);
        Parking6.setOwner(Tile.OWNER_UNOWNABLE);
        Parking6.image = R.drawable.i60;
        
        //North West
        
        Tile Parking7 = Parking6.addNextStop(Tile.DIRECTION_NORTHWEST);
        Parking7.setName("Parking Garage Level 2 - Section B");
        Parking7.setRegion(Tile.REGION_PARKING_SPACES);
        Parking7.setPrice(300);
        Parking7.setBaseRent(60);
        Parking7.setBaseRegionRent(30);
        Parking7.priceUpgrades(200, 300, 400, 500);
        Parking7.setOwner(Tile.OWNER_UNOWNABLE);
        Parking7.image = R.drawable.i61;
        
        Tile Parking8 = Parking7.addNextStop(Tile.DIRECTION_NORTHWEST);
        Parking8.setName("Parking Garage Level 2 - Section C");
        Parking8.setRegion(Tile.REGION_PARKING_SPACES);
        Parking8.setPrice(300);
        Parking8.setBaseRent(60);
        Parking8.setBaseRegionRent(30);
        Parking8.priceUpgrades(200, 300, 400, 500);
        Parking8.setOwner(Tile.OWNER_UNOWNABLE);
        Parking8.image = R.drawable.i62;
        
        Tile Parking9 = Parking8.addNextStop(Tile.DIRECTION_NORTHWEST);
        Parking9.setName("Parking Garage Level 2 - Section D");
        Parking9.setRegion(Tile.REGION_PARKING_SPACES);
        Parking9.setPrice(300);
        Parking9.setBaseRent(60);
        Parking9.setBaseRegionRent(30);
        Parking9.priceUpgrades(200, 300, 400, 500);
        Parking9.setOwner(Tile.OWNER_UNOWNABLE);
        Parking9.image = R.drawable.i63;
        
        Tile Parking10 = Parking9.addNextStop(Tile.DIRECTION_NORTHWEST);
        Parking10.setName("Parking Garage Level 2 - Section E");
        Parking10.setRegion(Tile.REGION_PARKING_SPACES);
        Parking10.setPrice(300);
        Parking10.setBaseRent(60);
        Parking10.setBaseRegionRent(30);
        Parking10.priceUpgrades(200, 300, 400, 500);
        Parking10.setOwner(Tile.OWNER_UNOWNABLE);
        Parking10.image = R.drawable.i64;
        
        // Athletic
        //North East
        
        Tile Athletic3 = Parking5.addNextStop(Tile.DIRECTION_WEST);
        Athletic3.setName("Football Field");
        Athletic3.setRegion(Tile.REGION_ATHLETIC_COMPLEX);
        Athletic3.setPrice(250);
        Athletic3.setBaseRent(50);
        Athletic3.setBaseRegionRent(25);
        Athletic3.priceUpgrades(187.5, 375, 562.5, 750);
        Parking10.addNextStop(Athletic3);
        Athletic3.image = R.drawable.i65;
        
        //North West
        
        Tile Athletic4 = Athletic3.addNextStop(Tile.DIRECTION_NORTHWEST);
        Athletic4.setName("Soccer Field");
        Athletic4.setRegion(Tile.REGION_ATHLETIC_COMPLEX);
        Athletic4.setPrice(250);
        Athletic4.setBaseRent(50);
        Athletic4.setBaseRegionRent(25);
        Athletic4.priceUpgrades(187.5, 375, 562.5, 750);
        Athletic4.image = R.drawable.i66;
        
        //West
        
        Tile Athletic5 = Athletic4.addNextStop(Tile.DIRECTION_WEST);
        Athletic5.setName("Track");
        Athletic5.setRegion(Tile.REGION_ATHLETIC_COMPLEX);
        Athletic5.setPrice(250);
        Athletic5.setBaseRent(50);
        Athletic5.setBaseRegionRent(25);
        Athletic5.priceUpgrades(187.5, 375, 562.5, 750);
        Athletic5.image = R.drawable.i67;
        
        Tile Athletic6 = Athletic5.addNextStop(Tile.DIRECTION_WEST);
        Athletic6.setName("Tennis Courts");
        Athletic6.setRegion(Tile.REGION_ATHLETIC_COMPLEX);
        Athletic6.setPrice(250);
        Athletic6.setBaseRent(50);
        Athletic6.setBaseRegionRent(25);
        Athletic6.priceUpgrades(187.5, 375, 562.5, 750);
        Athletic6.image = R.drawable.i68;
        
        Tile Field_Road_Shuttle = Athletic6.addNextStop(Tile.DIRECTION_WEST);
        Field_Road_Shuttle.setName("Field Road Shuttle Stop");
        Field_Road_Shuttle.setRegion(Tile.REGION_SHUTTLE);
        Field_Road_Shuttle.setPrice(250);
        Field_Road_Shuttle.setBaseRent(50);
        Field_Road_Shuttle.setBaseRegionRent(25);
        Field_Road_Shuttle.priceUpgrades(187.5, 375, 562.5, 750);
        Field_Road_Shuttle.image = R.drawable.i69;
        
        
        //LOwer campus suites
        //South West
        
        Tile Fenway = Field_Road_Shuttle.addNextStop(Tile.DIRECTION_SOUTHWEST);
        Fenway.setName("Fenway Hall");
        Fenway.setRegion(Tile.REGION_LOWER_CAMPUS_SUITES);
        Fenway.setPrice(200);
        Fenway.setBaseRent(40);
        Fenway.setBaseRegionRent(20);
        Fenway.priceUpgrades(150, 300, 450, 600);
        Fenway.image = R.drawable.i70;
        
        Tile Copley_North = Fenway.addNextStop(Tile.DIRECTION_SOUTHWEST);
        Copley_North.setName("Copley North");
        Copley_North.setRegion(Tile.REGION_LOWER_CAMPUS_SUITES);
        Copley_North.setPrice(200);
        Copley_North.setBaseRent(40);
        Copley_North.setBaseRegionRent(20);
        Copley_North.priceUpgrades(150, 300, 450, 600);
        Copley_North.image = R.drawable.i71;
        
        Tile Copley_South = Copley_North.addNextStop(Tile.DIRECTION_SOUTHWEST);
        Copley_South.setName("Copley South");
        Copley_South.setRegion(Tile.REGION_LOWER_CAMPUS_SUITES);
        Copley_South.setPrice(200);
        Copley_South.setBaseRent(40);
        Copley_South.setBaseRegionRent(20);
        Copley_South.priceUpgrades(150, 300, 450, 600);
        Copley_South.image = R.drawable.i72;
        
        // road
        //Bridge
        
        Tile Road10 = Copley_South.addNextStop(Tile.DIRECTION_SOUTHWEST);
        Road10.setName("Field Road");
        Road10.setRegion(Tile.REGION_PATHWAYS);
        Road10.setPrice(50);
        Road10.setBaseRent(50);
        Road10.setBaseRegionRent(50);
        Road10.priceUpgrades(200, 300, 500, 700);
        Road10.setOwner(Tile.OWNER_UNOWNABLE);
        Road10.image = R.drawable.i73;
                
        Tile Road11 = Road10.addNextStop(Tile.DIRECTION_WEST);
        Road11.setName("College Drive");
        Road11.setRegion(Tile.REGION_PATHWAYS);
        Road11.setPrice(50);
        Road11.setBaseRent(50);
        Road11.setBaseRegionRent(50);
        Road11.priceUpgrades(200, 300, 500, 700);
        Road11.addNextStop(Road10);
        Road11.setOwner(Tile.OWNER_UNOWNABLE);
        Road11.image = R.drawable.i74;
        
        Tile Callahan = Road11.addNextStop(Tile.DIRECTION_WEST);
        Callahan.setName("Callahan Police Station");
        Callahan.setRegion(Tile.REGION_SPECIAL);
        Callahan.setPrice(300);
        Callahan.setBaseRent(60);
        Callahan.setBaseRegionRent(30);
        Callahan.priceUpgrades(225, 450, 675, 900);
        Callahan.addNextStop(Greenspace);
        Callahan.addNextStop(Road11);
        Greenspace.addNextStop(Callahan);
        Callahan.setOwner(Tile.OWNER_UNOWNABLE);
        Callahan.image = R.drawable.i75;
        
        
        //Lower Campus APT
        //North East
        
        Tile Orchard_North = Road10.addNextStop(Tile.DIRECTION_SOUTHEAST);
        Orchard_North.setName("Orchard North");
        Orchard_North.setRegion(Tile.REGION_LOWER_CAMPUS_APT);
        Orchard_North.setPrice(250);
        Orchard_North.setBaseRent(50);
        Orchard_North.setBaseRegionRent(25);
        Orchard_North.priceUpgrades(187.5, 375, 562.5, 750);
        Orchard_North.image = R.drawable.i76;
        		
        Tile Orchard_South = Orchard_North.addNextStop(Tile.DIRECTION_SOUTHEAST);
        Orchard_South.setName("Orchard South");
        Orchard_South.setRegion(Tile.REGION_LOWER_CAMPUS_APT);
        Orchard_South.setPrice(250);
        Orchard_South.setBaseRent(50);
        Orchard_South.setBaseRegionRent(25);
        Orchard_South.priceUpgrades(187.5, 375, 562.5, 750);
        Orchard_South.image = R.drawable.i77;
        
        Tile Castle = Orchard_South.addNextStop(Tile.DIRECTION_SOUTHEAST);
        Castle.setName("Castle Hall");
        Castle.setRegion(Tile.REGION_LOWER_CAMPUS_APT);
        Castle.setPrice(250);
        Castle.setBaseRent(50);
        Castle.setBaseRegionRent(25);
        Castle.priceUpgrades(187.5, 375, 562.5, 750);
        Castle.addNextStop(Lowe_Campus);
        Castle.image = R.drawable.i78;
      
       
        Tile.setFacilitiesTile(Facilities_Management);
		Tile.setJailTile(Callahan);
	}
}
