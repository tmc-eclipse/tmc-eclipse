package fi.helsinki.cs.tmc.core.services;

/*public class CoursesTest {

 private CourseDAO courseDAO;
 private CourseStorage courseStorage;

 private List<Course> courseList = new ArrayList<Course>();
 private Course c1 = new Course("c1");
 private Course c2 = new Course("c2");
 private Course c3 = new Course("c3");

 @Before
 public void setUp() throws UserVisibleException {
 courseStorage = mock(CourseStorage.class);

 courseList.add(c1);
 courseList.add(c2);
 courseList.add(c3);

 when(courseStorage.load()).thenReturn(courseList);

 courseDAO = new CourseDAO(courseStorage);
 }

 @Test
 public void constructorLoadsCourses() {
 new CourseDAO(courseStorage);
 verify(courseStorage, times(2)).load(); // first in setup, then here
 }

 @Test
 public void getCoursesReturnCorrectList() {
 assertEquals(3, courseDAO.getCourses().size());
 assertTrue(courseDAO.getCourses().contains(c1));
 assertTrue(courseDAO.getCourses().contains(c2));
 assertTrue(courseDAO.getCourses().contains(c3));
 }

 @Test
 public void loadCoursesRefreshesFromDAO() {
 courseDAO.loadCourses();
 verify(courseStorage, times(2)).load(); // first in setup, then here
 }

 @Test
 public void setCourseListSavesToDAO() {
 List<Course> newList = new ArrayList<Course>();
 courseDAO.setCourses(newList);
 verify(courseStorage, times(1)).save(newList);
 assertEquals(0, courseDAO.getCourses().size());
 }

 @Test
 public void updateCourseSavesToDAO() {
 courseDAO.updateCourse(new Course("c1"));
 verify(courseStorage, times(1)).save(any(List.class));
 }

 @Test
 public void updateCourseOverwritesCourseWithSameName() {
 c1.setId(1337);
 courseDAO.updateCourse(new Course("c2"));
 assertTrue(1337 != courseDAO.getCourseByName("c2").getId());
 }

 @Test
 public void updateCourseDoesNotOverwriteIfNoCourseWithSameNameIsFound() {
 courseDAO.updateCourse(new Course("c15"));
 assertNull(courseDAO.getCourseByName("c15"));
 }

 @Test
 public void getCourseByNameReturnsCorrectCourseWhenPresent() {
 assertEquals(c2, courseDAO.getCourseByName("c2"));
 }

 @Test
 public void getCoursesByNameReturnsNullIfNoSuchCoursePresent() {
 assertNull(courseDAO.getCourseByName("asd"));
 }

 @Test
 public void getCoursesByNameReturnsNullIfNoCourses() {
 courseDAO.setCourses(new ArrayList<Course>());
 assertNull(courseDAO.getCourseByName("c1"));
 }

 }*/
