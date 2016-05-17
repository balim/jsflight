package com.focusit.scenario;

import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.focusit.jsflight.player.config.Configuration;
import com.focusit.jsflight.player.scenario.UserScenario;
import com.focusit.model.Event;
import com.focusit.model.Experiment;
import com.focusit.repository.EventRepository;
import com.focusit.repository.ExperimentRepository;

/**
 * Created by doki on 12.05.16.
 */
public class MongoDbScenario extends UserScenario
{
    private final Experiment experiment;
    private EventRepository repository;
    private ExperimentRepository experimentRepository;

    public MongoDbScenario(Experiment experiment, EventRepository repository, ExperimentRepository experimentRepository)
    {
        this.experiment = experiment;
        this.repository = repository;
        this.experimentRepository = experimentRepository;
    }

    @Override
    public Configuration getConfiguration()
    {
        return experiment.getConfiguration();
    }

    @Override
    public int getPosition()
    {
        return experiment.getPosition();
    }

    @Override
    public int getStepsCount()
    {
        return experiment.getSteps();
    }

    @Override
    public JSONObject getStepAt(int position)
    {
        Page<Event> page = repository.findOneByRecordingId(new ObjectId(experiment.getRecordingId()),
                new PageRequest(position, 1, new Sort(Sort.Direction.ASC, "timestamp")));
        Event event = page.getContent().get(0);
        JSONObject object = new JSONObject(event);
        return object;
    }

    @Override
    public void next()
    {
        setPosition(getPosition() + 1);
        if (getPosition() == getStepsCount())
        {
            setPosition(0);
        }
    }

    @Override
    public String getScenarioFilename()
    {
        return getRecordingName();
    }

    public String getRecordingId()
    {
        return experiment.getRecordingId();
    }

    public String getRecordingName()
    {
        return experiment.getRecordingName();
    }

    public String getTag()
    {
        return experiment.getTag();
    }

    public String getTagHash()
    {
        return experiment.getTagHash();
    }

    public String getExperimentId()
    {
        return experiment.getId();
    }

    public int getFirstStep()
    {
        return experiment.getPosition();
    }

    public int getMaxStep()
    {
        return experiment.getLimit();
    }

    public int getProxyPort()
    {
        return Integer.parseInt(experiment.getConfiguration().getCommonConfiguration().getProxyPort());
    }

    @Override
    public void setPosition(int position)
    {
        experiment.setPosition(position);
        experimentRepository.save(experiment);
    }
}