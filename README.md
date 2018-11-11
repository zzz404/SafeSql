SafeSql

Java

        from(Document.class, User.class).select((d, u) -> {
            d.getId();
            d.getTitle();
            u.getName();
        }).where((d, u) -> {
            cond(d.getOwnerId(), "=", u.getId());
            cond(d.getId(), IN, 1,3,5).or(d.getTitle(), LIKE, "abc%");
        }).orderBy((d, u) -> {
            asc(u.getName());
            asc(d.getTitle());
        }).offset(20).limit(10).queryStream();

means

SELECT d.id, d.title, u.name
FROM Document d, User u
WHERE d.ownerId=u.id AND (d.id IN (1,3,5) OR d.title LIKE 'abc%')
ORDER BY u.name, d.title
LIMIT 20, 10
