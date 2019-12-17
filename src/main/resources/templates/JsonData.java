package [(${packageName})]

import java.util.*;
[# th:if="${lombok}"]
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;[/]
[# th:if="${jpa}"]import javax.persistence.Column;[/]
[# th:if="${lombok}"]@Data
@EqualsAndHashCode
@ToString[/]

public class [(${className})]{
[# th:each="field : ${fields}"][# th:if="${jpa}"]
    @Column(name = "[(${field.originalName})]")[/]
    private [(${field.type})] [(${field.name})];
    [# th:if="!${lombok}"]
    public [(${field.type})] [(${field.type=='Boolean'?'is':'get'})][(${field.capitalName})]() {
        return [(${field.name})];
    }

    public void set[(${field.capitalName})]([(${field.type})] [(${field.name})]) {
        this.[(${field.name})] = [(${field.name})];
    }[/]
[/]
}


