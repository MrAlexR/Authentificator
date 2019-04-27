public class StringCheck
{
	public static Boolean IsValidString(String value)
	{
		if(value.isEmpty()) return false;
		if(value.trim().isEmpty()) return false;
		return true;
	}
}
