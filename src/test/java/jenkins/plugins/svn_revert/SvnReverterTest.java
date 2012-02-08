package jenkins.plugins.svn_revert;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.scm.SubversionSCM;
import hudson.scm.SubversionSCM.DescriptorImpl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

@SuppressWarnings("rawtypes")
public class SvnReverterTest extends AbstractMockitoTestCase {

    private SvnReverter reverter;

    @Mock
    private Messenger messenger;
    @Mock
    private AbstractBuild build;
    @Mock
    private AbstractBuild rootBuild;
    @Mock
    private AbstractProject rootProject;
    @Mock
    private AbstractProject project;
    @Mock
    private BuildListener listener;
    @Mock
    private SubversionSCM subversionScm;
    @Mock
    private DescriptorImpl subversionDescriptor;

    @Before
    public void setup() {
        when(build.getRootBuild()).thenReturn(rootBuild);
        when(build.getProject()).thenReturn(project);
        when(project.getRootProject()).thenReturn(rootProject);
        reverter = new SvnReverter(build, listener, messenger);
    }

    @Test
    public void shouldLogIfRepoIsNotSubversion() throws Exception {
        reverter.revert();
        verify(messenger).informNotSubversionSCM();
    }

    @Test
    public void shouldReturnTrueIfRepoIsNotSubversion() throws Exception {
        assertThat(reverter.revert(), is(true));
    }

    @Test
    public void shouldLogIfNoSvnAuthAvailable() throws Exception {
        givenSubversionScmWithNoAuth();
        reverter.revert();
        verify(messenger).informNoSvnAuthProvider();
    }

    @Test
    public void shouldFailIfNoSvnAuthAvailable() throws Exception {
        givenSubversionScmWithNoAuth();
        assertThat(reverter.revert(), is(false));
    }

    public void givenSubversionScmWithNoAuth() {
        when(rootProject.getScm()).thenReturn(subversionScm);
        when(subversionScm.getDescriptor()).thenReturn(subversionDescriptor);
    }

}
