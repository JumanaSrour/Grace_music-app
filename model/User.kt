package iug.jumana.grace.model

class User(var id:Int,var name:String,var email:String,var password:String,image:String) {
    companion object {
        val COL_ID="id"
        val COL_NAME = "name"
        val COL_EMAIL = "email"
        val COL_PASSWORD = "password"
        val COL_IMG="image"

        val TABLE_NAME = "User"
        val TABLE_CREATE = "create table $TABLE_NAME ($COL_ID integer primary key autoincrement,$COL_NAME text not null," +
                "$COL_EMAIL text not null, $COL_PASSWORD integer,$COL_IMG text)"
    }
}