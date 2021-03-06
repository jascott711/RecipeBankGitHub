package com.rb.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

//owner: Huijun Sun
public class RecipeOperation {

	// return recipeId
	public static int createRecipe(HashMap<String, String> recipeHashMap) {

		Connection connection = null;
		PreparedStatement st = null;
		ResultSet rs=null;
		int recipeId = 0;
		String sql = "insert into recipebank.recipe (accountid,recipeTitle,Description,photo) values(?,?,?,?);";
				

		try {

			connection = ConnectDB.getConnection();
			st = connection.prepareStatement(sql);

			st.setInt(1, Integer.parseInt(recipeHashMap.get("AccountId")));
			st.setString(2, recipeHashMap.get("RecipeTitle"));
			st.setString(3, recipeHashMap.get("Description"));
			byte[] b = recipeHashMap.get("photo").getBytes();
			st.setBytes(4, b);
			st.executeUpdate();
			rs = st.executeQuery("select LAST_INSERT_ID() from recipebank.recipe limit 1;");
			if (rs.next()) {
				recipeId=rs.getInt(1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ConnectDB.closeConnection(connection);
		}

		return recipeId;
	}

	public static boolean InsertIngredients(
			ArrayList<HashMap<String, String>> ingredients) {
		boolean state = false;
		Connection connection = null;
		PreparedStatement st = null;

		try {
			connection = ConnectDB.getConnection();

			String sqlString = "";
			for (int i = 0; i < ingredients.size(); i++) {
				HashMap<String, String> ingMap = ingredients.get(i);
				sqlString += "insert into recipebank.ingredient (ingredientName) values('"
						+ ingMap.get("ingredientName") + "');";
				sqlString += "insert into recipebank.recipeingredientlist (RecipeId,ingredientId,ingredientMeasure,ingredientQuanlity) "
						+ "values("
						+ ingMap.get("RecipeId")
						+ ",(select LAST_INSERT_ID() from recipebank.ingredient limit 1 ),'"
						+ ingMap.get("ingredientMeasure")
						+ "',"
						+ ingMap.get("ingredientQuanlity") + ");";
			}

			System.out.println(sqlString);
			st.addBatch(sqlString);
			st.executeBatch();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ConnectDB.closeConnection(connection);
		}

		return state;
	}

	public static boolean insertSteps(
			ArrayList<HashMap<String, String>> stepsList) {
		boolean state = false;

		Connection connection = null;
		PreparedStatement st = null;

		try {
			connection = ConnectDB.getConnection();

			String sqlString = "";
			for (int i = 0; i < stepsList.size(); i++) {
				HashMap<String, String> stepMaps = stepsList.get(i);
				sqlString += "insert into recipebank.recipestep (StepDesc,StepPhoto,RecipeId,stepTime) "
						+ "values('"
						+ stepMaps.get("StepDesc")
						+ "','"
						+ stepMaps.get("StepPhoto").getBytes()
						+ "',"
						+ stepMaps.get("RecipeId")
						+ ","
						+ stepMaps.get("stepTime") + ");";
			}

			System.out.println(sqlString);
			st.addBatch(sqlString);
			st.executeBatch();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ConnectDB.closeConnection(connection);
		}

		return state;

	}
	
}
