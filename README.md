# CookAid
🍽️ Mobile cooking assistant with the database of recipes and step-by-step guide feature, written in Java.

***

## External data retrieving

Recipes with all details are fetched from external source using HTTP 
requests.
Free, open-source, developed by users database of recipes and meals 
gathered from the Internet. Easy-to-use API is available under the URL:
https://themealdb.com/api/json/v1/1

## Local database

Recipes created by user or added to the list or favorites are kept in the local database, 
implemented using Room.The only entity used is Recipe, fields containing lists are converted to JSON strings

| Column Name   | Type                | Constraints |
|---------------|---------------------|-------------|
| id            | String              | Primary Key |
| name          | String              |             |
| category      | String              |             |
| cuisine       | String              |             |
| tags          | List\<String\>      |             |
| imageUrl      | String              |             |
| ingredients   | List\<Ingredient\>  |             |
| instructions  | String              |             |
| isOwn         | boolean             |             |
